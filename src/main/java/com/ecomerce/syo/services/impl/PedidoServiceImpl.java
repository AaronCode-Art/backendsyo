package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.pedido.*;
import com.ecomerce.syo.execption.ResourceNotFoundException;
import com.ecomerce.syo.model.*;
import com.ecomerce.syo.repository.*;
import com.ecomerce.syo.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository               pedidoRepository;
    private final DetallePedidoRepository         detalleRepository;
    private final HistorialEstadoPedidoRepository historialRepository;
    private final PagoRepository                  pagoRepository;
    private final EstadoPedidoRepository          estadoRepository;
    private final ClienteRepository               clienteRepository;
    private final ProductoRepository              productoRepository;
    private final CarritoRepository               carritoRepository;
    private final CarritoItemRepository           carritoItemRepository;
    private final DireccionPedidoRepository       direccionRepository;

    // Nombres exactos de estado en BD
    private static final String PENDIENTE      = "Pendiente";
    private static final String PAGADO         = "Pagado";
    private static final String EN_PREPARACION = "En preparacion";

    // ── 1. CONFIRMAR PEDIDO ───────────────────────────────────────────────────
    // Lee el carrito del cliente → crea pedido+detalles+pago → vacía el carrito
    @Override
    @Transactional
    public PedidoDetalleDTO confirmarPedido(ConfirmarPedidoDTO dto, String correoAutenticado) {

        Cliente cliente = obtenerCliente(correoAutenticado);

        // Obtener carrito con items
        Carrito carrito = carritoRepository
                .findByClienteIdWithItems(cliente.getIdcliente())
                .orElseThrow(() -> new RuntimeException("No tienes productos en el carrito"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Validar campos obligatorios
        validarTipoEntrega(dto.getTipoentrega());
        validarTipoComprobante(dto.getTipocomprobante());
        validarMetodoPago(dto.getMetodopago());

        if ("delivery".equals(dto.getTipoentrega()) && dto.getDireccion() == null) {
            throw new RuntimeException("Se requiere dirección de entrega para delivery");
        }

        // Guardar dirección snapshot si es delivery
        DireccionPedido direccionSnapshot = null;
        if ("delivery".equals(dto.getTipoentrega()) && dto.getDireccion() != null) {
            direccionSnapshot = direccionRepository.save(DireccionPedido.builder()
                    .direccion(dto.getDireccion().getDireccion())
                    .referencia(dto.getDireccion().getReferencia())
                    .distrito(dto.getDireccion().getDistrito())
                    .codigopostal(dto.getDireccion().getCodigopostal())
                    .build());
        }

        // Crear cabecera del pedido en estado "Pendiente"
        EstadoPedido estadoPendiente = obtenerEstado(PENDIENTE);
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estado(estadoPendiente)
                .tipoentrega(dto.getTipoentrega())
                .tipocomprobante(dto.getTipocomprobante())
                .total(BigDecimal.ZERO)
                .nroticket(generarTicket())
                .build();
        pedido = pedidoRepository.save(pedido);

        // Crear detalles desde los items del carrito
        BigDecimal total = BigDecimal.ZERO;
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                        + ". Disponible: " + producto.getStock());
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal descuento = producto.getDescuento() != null ? producto.getDescuento() : BigDecimal.ZERO;
            BigDecimal factor = BigDecimal.ONE.subtract(
                    descuento.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));
            BigDecimal precioTotal = precioUnitario
                    .multiply(new BigDecimal(item.getCantidad()))
                    .multiply(factor)
                    .setScale(2, RoundingMode.HALF_UP);

            detalleRepository.save(DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .preciounitario(precioUnitario)
                    .descuento(descuento)
                    .preciototal(precioTotal)
                    .direccion(direccionSnapshot)
                    .build());

            // Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            total = total.add(precioTotal);
        }

        // Actualizar total del pedido
        pedido.setTotal(total);
        pedidoRepository.save(pedido);

        // Registrar historial: estado inicial "Pendiente"
        registrarHistorial(pedido, estadoPendiente);

        // Registrar pago
        pagoRepository.save(Pago.builder()
                .pedido(pedido)
                .estadopago("completado")
                .metodopago(dto.getMetodopago())
                .build());

        // Avanzar estado: Pendiente → Pagado → En preparacion
        EstadoPedido estadoPagado = obtenerEstado(PAGADO);
        pedido.setEstado(estadoPagado);
        pedidoRepository.save(pedido);
        registrarHistorial(pedido, estadoPagado);

        EstadoPedido estadoEnPrep = obtenerEstado(EN_PREPARACION);
        pedido.setEstado(estadoEnPrep);
        pedidoRepository.save(pedido);
        registrarHistorial(pedido, estadoEnPrep);

        // Vaciar el carrito
        // Se limpia la colección del objeto ya cargado en sesión y se persiste.
        // Esto activa el orphanRemoval = true definido en Carrito.items,
        // que elimina todos los CarritoItem en un solo DELETE en la misma transacción.
        // No se usa deleteAllByCarrito_Idcarrito() directamente porque Hibernate
        // ya tiene los items en memoria y puede lanzar conflictos de sesión.
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return toDetalleDTO(pedido);
    }

    // ── 2. MIS PEDIDOS ────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumenDTO> misPedidos(String correoAutenticado) {
        Cliente cliente = obtenerCliente(correoAutenticado);
        return pedidoRepository.findAllByCliente_IdclienteOrderByFechaDesc(cliente.getIdcliente())
                .stream().map(this::toResumenDTO).collect(Collectors.toList());
    }

    // ── 3. DETALLE DE UN PEDIDO ───────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public PedidoDetalleDTO obtenerDetalle(UUID pedidoid, String correoAutenticado) {
        Pedido pedido = pedidoRepository.findById(pedidoid)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
        validarPropietario(pedido.getCliente().getCorreo(), correoAutenticado);
        return toDetalleDTO(pedido);
    }

    // ── 4. PEDIDOS POR ESTADO ─────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumenDTO> pedidosPorEstado(String estado, String correoAutenticado) {
        Cliente cliente = obtenerCliente(correoAutenticado);
        return pedidoRepository.findByClienteAndEstado(cliente.getIdcliente(), estado)
                .stream().map(this::toResumenDTO).collect(Collectors.toList());
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private EstadoPedido obtenerEstado(String nombre) {
        return estadoRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado en BD: " + nombre));
    }

    private void registrarHistorial(Pedido pedido, EstadoPedido estado) {
        historialRepository.save(HistorialEstadoPedido.builder()
                .pedido(pedido).estado(estado).build());
    }

    private Cliente obtenerCliente(String correo) {
        return clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    private void validarPropietario(String correoRecurso, String correoAutenticado) {
        if (!correoRecurso.equals(correoAutenticado)) {
            throw new RuntimeException("No tienes permiso para acceder a este recurso");
        }
    }

    // Genera ticket "S-XXXXX" (5 dígitos aleatorios)
    private String generarTicket() {
        int numero = 10000 + new Random().nextInt(90000);
        return "S-" + numero;
    }

    private void validarTipoEntrega(String v) {
        if (v == null || (!v.equals("delivery") && !v.equals("recojo")))
            throw new RuntimeException("tipoentrega debe ser 'delivery' o 'recojo'");
    }

    private void validarTipoComprobante(String v) {
        if (v == null || (!v.equals("boleta") && !v.equals("factura")))
            throw new RuntimeException("tipocomprobante debe ser 'boleta' o 'factura'");
    }

    private void validarMetodoPago(String v) {
        List<String> validos = List.of("tarjeta_credito","tarjeta_debito","yape","plin","transferencia","efectivo");
        if (v == null || !validos.contains(v))
            throw new RuntimeException("metodopago inválido: " + v);
    }

    // ── MAPEOS ────────────────────────────────────────────────────────────────

    private PedidoResumenDTO toResumenDTO(Pedido p) {
        return PedidoResumenDTO.builder()
                .idpedido(p.getIdpedido())
                .nroticket(p.getNroticket())
                .fecha(p.getFecha())
                .estado(p.getEstado().getNombre())
                .tipoentrega(p.getTipoentrega())
                .tipocomprobante(p.getTipocomprobante())
                .total(p.getTotal())
                .cantidadItems(detalleRepository.findAllByPedido_Idpedido(p.getIdpedido()).size())
                .build();
    }

    private PedidoDetalleDTO toDetalleDTO(Pedido p) {

        List<PedidoItemDTO> items = detalleRepository
                .findAllByPedido_Idpedido(p.getIdpedido()).stream()
                .map(d -> PedidoItemDTO.builder()
                        .iddetalle(d.getIddetalle())
                        .productoid(d.getProducto().getIdproducto())
                        .nombreProducto(d.getProducto().getNombre())
                        .imgurl(d.getProducto().getImgurl())
                        .cantidad(d.getCantidad())
                        .preciounitario(d.getPreciounitario())
                        .descuento(d.getDescuento())
                        .preciototal(d.getPreciototal())
                        .build())
                .collect(Collectors.toList());

        List<PedidoHistorialDTO> historial = historialRepository
                .findAllByPedido_IdpedidoOrderByFechaAsc(p.getIdpedido()).stream()
                .map(h -> PedidoHistorialDTO.builder()
                        .estado(h.getEstado().getNombre())
                        .fecha(h.getFecha())
                        .build())
                .collect(Collectors.toList());

        PedidoPagoDTO pagoDTO = pagoRepository.findByPedido_Idpedido(p.getIdpedido())
                .map(pago -> PedidoPagoDTO.builder()
                        .idpago(pago.getIdpago())
                        .fechapago(pago.getFechapago())
                        .estadopago(pago.getEstadopago())
                        .metodopago(pago.getMetodopago())
                        .build())
                .orElse(null);

        // Dirección del primer detalle que tenga (todos comparten la misma)
        DireccionPedidoDTO direccionDTO = detalleRepository
                .findAllByPedido_Idpedido(p.getIdpedido()).stream()
                .filter(d -> d.getDireccion() != null)
                .findFirst()
                .map(d -> DireccionPedidoDTO.builder()
                        .iddireccion(d.getDireccion().getIddireccion())
                        .direccion(d.getDireccion().getDireccion())
                        .referencia(d.getDireccion().getReferencia())
                        .distrito(d.getDireccion().getDistrito())
                        .codigopostal(d.getDireccion().getCodigopostal())
                        .build())
                .orElse(null);

        return PedidoDetalleDTO.builder()
                .idpedido(p.getIdpedido())
                .nroticket(p.getNroticket())
                .fecha(p.getFecha())
                .estadoActual(p.getEstado().getNombre())
                .tipoentrega(p.getTipoentrega())
                .tipocomprobante(p.getTipocomprobante())
                .total(p.getTotal())
                .items(items)
                .historial(historial)
                .pago(pagoDTO)
                .direccion(direccionDTO)
                .build();
    }
}
