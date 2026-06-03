package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.pedido.PedidoResponseDTO;
import com.ecomerce.syo.dto.pedido.DetallePedidoResponseDTO;
import com.ecomerce.syo.model.*;
import com.ecomerce.syo.repository.PedidoRepository;
import com.ecomerce.syo.repository.CarritoRepository;
import com.ecomerce.syo.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_CANCELADO = "CANCELADO";

    @Override
    @Transactional
    public PedidoResponseDTO crearPedidoDesdeCarrito(UUID clienteId, String tipoEntrega, String tipoComprobante) {

        Carrito carrito = carritoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RuntimeException("No se encontró carrito para el cliente"));

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        EstadoPedido estadoPendiente = pedidoRepository.findEstadoByNombre(ESTADO_PENDIENTE)
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));

        Pedido pedido = Pedido.builder()
                .cliente(carrito.getCliente())
                .estado(estadoPendiente)
                .tipoentrega(tipoEntrega)
                .tipocomprobante(tipoComprobante)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal totalPedido = BigDecimal.ZERO;

        for (CarritoDetalle item : carrito.getItems()) {
            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(item.getProducto())
                    .cantidad(item.getCantidad())
                    .preciounitario(item.getProducto().getPreciodesct())
                    .descuento(item.getProducto().getDescuento() != null ? item.getProducto().getDescuento() : BigDecimal.ZERO)
                    .preciototal(item.getProducto().getPreciodesct().multiply(BigDecimal.valueOf(item.getCantidad())))
                    .build();

            pedido.getDetalles().add(detalle);
            totalPedido = totalPedido.add(detalle.getPreciototal());
        }

        pedido.setTotal(totalPedido);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return convertirAResponseDTO(pedidoGuardado);
    }

    @Override
    public List<PedidoResponseDTO> obtenerPorCliente(UUID clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return pedidos.stream().map(this::convertirAResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> obtenerPorEstado(UUID estadoId) {
        List<Pedido> pedidos = pedidoRepository.findByEstadoId(estadoId);
        return pedidos.stream().map(this::convertirAResponseDTO).collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO obtenerDetalle(UUID pedidoId) {
        Pedido pedido = pedidoRepository.findByIdWithDetalles(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return convertirAResponseDTO(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO cambiarEstado(UUID pedidoId, UUID nuevoEstadoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido nuevoEstado = pedidoRepository.findEstadoById(nuevoEstadoId)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        pedido.setEstado(nuevoEstado);
        Pedido actualizado = pedidoRepository.save(pedido);

        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO cancelarPedido(UUID pedidoId) {
        EstadoPedido estadoCancelado = pedidoRepository.findEstadoByNombre(ESTADO_CANCELADO)
                .orElseThrow(() -> new RuntimeException("Estado CANCELADO no encontrado"));

        return cambiarEstado(pedidoId, estadoCancelado.getIdestado());
    }

    private PedidoResponseDTO convertirAResponseDTO(Pedido pedido) {
        List<DetallePedidoResponseDTO> detallesDTO = pedido.getDetalles().stream()
                .map(d -> DetallePedidoResponseDTO.builder()
                        .productoId(d.getProducto().getIdproducto())
                        .nombreProducto(d.getProducto().getNombre())
                        .cantidad(d.getCantidad())
                        .preciounitario(d.getPreciounitario())
                        .preciototal(d.getPreciototal())
                        .build())
                .collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .idpedido(pedido.getIdpedido())
                .clienteNombre(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido())
                .estado(pedido.getEstado().getNombre())
                .fecha(pedido.getFecha())
                .tipoEntrega(pedido.getTipoentrega())
                .tipoComprobante(pedido.getTipocomprobante())
                .total(pedido.getTotal())
                .detalles(detallesDTO)
                .build();
    }
}