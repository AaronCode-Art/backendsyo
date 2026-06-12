package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.carrito.*;
import com.ecomerce.syo.execption.ResourceNotFoundException;
import com.ecomerce.syo.model.*;
import com.ecomerce.syo.repository.*;
import com.ecomerce.syo.services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository       carritoRepository;
    private final CarritoItemRepository   carritoItemRepository;
    private final ClienteRepository       clienteRepository;
    private final ProductoRepository      productoRepository;

    // ── 1. OBTENER CARRITO ────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerCarrito(String correoAutenticado) {
        Cliente cliente = obtenerCliente(correoAutenticado);

        // Si no tiene carrito aún devuelve uno vacío (sin crear nada en BD)
        return carritoRepository.findByClienteIdWithItems(cliente.getIdcliente())
                .map(this::toDTO)
                .orElse(carritoVacio());
    }

    // ── 2. AGREGAR PRODUCTO (upsert) ──────────────────────────────────────────
    @Override
    @Transactional
    public CarritoResponseDTO agregarProducto(AgregarCarritoDTO dto, String correoAutenticado) {
        if (dto.getCantidad() == null || dto.getCantidad() < 1) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        Cliente cliente  = obtenerCliente(correoAutenticado);
        Producto producto = productoRepository.findById(dto.getProductoid())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (producto.getStock() < dto.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
        }

        // Obtener o crear el carrito del cliente
        Carrito carrito = carritoRepository.findByCliente_Idcliente(cliente.getIdcliente())
                .orElseGet(() -> {
                    Carrito nuevo = Carrito.builder().cliente(cliente).build();
                    return carritoRepository.save(nuevo);
                });

        // Upsert: si el producto ya está en el carrito → suma cantidad
        carritoItemRepository
                .findByCarrito_IdcarritoAndProducto_Idproducto(carrito.getIdcarrito(), dto.getProductoid())
                .ifPresentOrElse(
                        item -> {
                            int nuevaCantidad = item.getCantidad() + dto.getCantidad();
                            if (producto.getStock() < nuevaCantidad) {
                                throw new RuntimeException("Stock insuficiente");
                            }
                            item.setCantidad(nuevaCantidad);
                            carritoItemRepository.save(item);
                        },
                        () -> carritoItemRepository.save(CarritoItem.builder()
                                .carrito(carrito)
                                .producto(producto)
                                .cantidad(dto.getCantidad())
                                .build())
                );

        return toDTO(carritoRepository.findByClienteIdWithItems(cliente.getIdcliente()).orElse(carrito));
    }

    // ── 3. ACTUALIZAR CANTIDAD DE UN ITEM ─────────────────────────────────────
    @Override
    @Transactional
    public CarritoResponseDTO actualizarItem(UUID itemid, ActualizarItemDTO dto, String correoAutenticado) {
        Cliente cliente = obtenerCliente(correoAutenticado);
        CarritoItem item = carritoItemRepository.findById(itemid)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

        // Verificar que el item pertenece al carrito del cliente autenticado
        if (!item.getCarrito().getCliente().getIdcliente().equals(cliente.getIdcliente())) {
            throw new RuntimeException("No tienes permiso para modificar este item");
        }

        if (dto.getCantidad() == null || dto.getCantidad() < 1) {
            // Si la cantidad es 0 o menos → eliminar el item
            carritoItemRepository.delete(item);
        } else {
            if (item.getProducto().getStock() < dto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }
            item.setCantidad(dto.getCantidad());
            carritoItemRepository.save(item);
        }

        return toDTO(carritoRepository.findByClienteIdWithItems(cliente.getIdcliente())
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado")));
    }

    // ── 4. ELIMINAR ITEM ──────────────────────────────────────────────────────
    @Override
    @Transactional
    public CarritoResponseDTO eliminarItem(UUID itemid, String correoAutenticado) {
        Cliente cliente = obtenerCliente(correoAutenticado);
        CarritoItem item = carritoItemRepository.findById(itemid)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

        if (!item.getCarrito().getCliente().getIdcliente().equals(cliente.getIdcliente())) {
            throw new RuntimeException("No tienes permiso para eliminar este item");
        }

        carritoItemRepository.delete(item);

        return carritoRepository.findByClienteIdWithItems(cliente.getIdcliente())
                .map(this::toDTO)
                .orElse(carritoVacio());
    }

    // ── 5. VACIAR CARRITO ─────────────────────────────────────────────────────
    @Override
    @Transactional
    public void vaciar(String correoAutenticado) {
        Cliente cliente = obtenerCliente(correoAutenticado);
        carritoRepository.findByCliente_Idcliente(cliente.getIdcliente())
                .ifPresent(carrito ->
                        carritoItemRepository.deleteAllByCarrito_Idcarrito(carrito.getIdcarrito()));
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private Cliente obtenerCliente(String correo) {
        return clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    private CarritoResponseDTO carritoVacio() {
        return CarritoResponseDTO.builder()
                .items(List.of())
                .total(BigDecimal.ZERO)
                .cantidadProductos(0)
                .build();
    }

    private CarritoResponseDTO toDTO(Carrito carrito) {
        List<CarritoItemResponseDTO> items = (carrito.getItems() == null ? List.<CarritoItem>of() : carrito.getItems())
                .stream()
                .map(item -> {
                    Producto p = item.getProducto();
                    BigDecimal preciodesct = p.getPreciodesct() != null ? p.getPreciodesct() : p.getPrecio();
                    BigDecimal subtotal = preciodesct.multiply(new BigDecimal(item.getCantidad()));
                    return CarritoItemResponseDTO.builder()
                            .iditem(item.getIditem())
                            .productoid(p.getIdproducto())
                            .nombre(p.getNombre())
                            .marca(p.getMarca())
                            .imgurl(p.getImgurl())
                            .precioUnitario(p.getPrecio())
                            .preciodesct(preciodesct)
                            .descuento(p.getDescuento())
                            .cantidad(item.getCantidad())
                            .subtotal(subtotal)
                            .stockDisponible(p.getStock())
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CarritoItemResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int cantidadProductos = items.stream()
                .mapToInt(CarritoItemResponseDTO::getCantidad)
                .sum();

        return CarritoResponseDTO.builder()
                .idcarrito(carrito.getIdcarrito())
                .fechacreacion(carrito.getFechacreacion())
                .items(items)
                .total(total)
                .cantidadProductos(cantidadProductos)
                .build();
    }
}
