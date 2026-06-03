package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.carrito.CarritoItemDTO;
import com.ecomerce.syo.dto.carrito.CarritoResponseDTO;
import com.ecomerce.syo.model.Carrito;
import com.ecomerce.syo.model.CarritoDetalle;
import com.ecomerce.syo.model.Cliente;
import com.ecomerce.syo.model.Producto;
import com.ecomerce.syo.repository.CarritoRepository;
import com.ecomerce.syo.repository.ClienteRepository;
import com.ecomerce.syo.repository.ProductoRepository;
import com.ecomerce.syo.services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

        private final CarritoRepository carritoRepository;
        private final ClienteRepository clienteRepository;
        private final ProductoRepository productoRepository;

        @Override
        @Transactional
        public CarritoResponseDTO agregarProducto(UUID clienteId, UUID productoId, Integer cantidad) {
                // Buscar o crear carrito del cliente
                Carrito carrito = carritoRepository.findByClienteIdWithItems(clienteId)
                                .orElseGet(() -> {

                                        Cliente cliente = clienteRepository.findById(clienteId)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Cliente no encontrado"));

                                        Carrito nuevoCarrito = Carrito.builder()
                                                        .cliente(cliente)
                                                        .fechacreacion(LocalDateTime.now())
                                                        .items(new java.util.ArrayList<>())
                                                        .build();

                                        return carritoRepository.save(nuevoCarrito);
                                });
                Producto producto = productoRepository.findById(productoId)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Verificar si el producto ya existe en el carrito
                CarritoDetalle itemExistente = carrito.getItems().stream()
                                .filter(item -> item.getProducto().getIdproducto().equals(productoId))
                                .findFirst()
                                .orElse(null);

                if (itemExistente != null) {
                        // Aumentar cantidad
                        itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
                } else {
                        // Crear nuevo item
                        CarritoDetalle nuevoItem = CarritoDetalle.builder()
                                        .carrito(carrito)
                                        .producto(producto)
                                        .cantidad(cantidad)
                                        .build();
                        carrito.getItems().add(nuevoItem);
                }

                carritoRepository.save(carrito);
                return convertirACarritoResponseDTO(carrito);
        }

        @Override
        @Transactional
        public CarritoResponseDTO cambiarCantidad(UUID clienteId, UUID productoId, Integer nuevaCantidad) {
                Carrito carrito = carritoRepository.findByClienteIdWithItems(clienteId)
                                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

                CarritoDetalle item = carrito.getItems().stream()
                                .filter(i -> i.getProducto().getIdproducto().equals(productoId))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

                item.setCantidad(nuevaCantidad);
                carritoRepository.save(carrito);
                return convertirACarritoResponseDTO(carrito);
        }

        @Override
        @Transactional
        public CarritoResponseDTO eliminarProducto(UUID clienteId, UUID productoId) {
                Carrito carrito = carritoRepository.findByClienteIdWithItems(clienteId)
                                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

                carrito.getItems().removeIf(item -> item.getProducto().getIdproducto().equals(productoId));
                carritoRepository.save(carrito);
                return convertirACarritoResponseDTO(carrito);
        }

        @Override
        @Transactional
        public CarritoResponseDTO obtenerCarrito(UUID clienteId) {
                Carrito carrito = carritoRepository.findByClienteIdWithItems(clienteId)
                                .orElseGet(() -> {
                                        // Crear carrito vacío si no existe
                                        Cliente cliente = clienteRepository.findById(clienteId)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Cliente no encontrado"));
                                        Carrito nuevo = Carrito.builder().cliente(cliente)
                                                        .fechacreacion(LocalDateTime.now()).build();
                                        return carritoRepository.save(nuevo);
                                });

                return convertirACarritoResponseDTO(carrito);
        }

        @Override
        @Transactional
        public void vaciarCarrito(UUID clienteId) {

                carritoRepository.findByClienteIdWithItems(clienteId).ifPresent(carrito -> {
                        carrito.getItems().clear();
                        carritoRepository.save(carrito);
                });
        }

        // Método privado para convertir entidad a DTO

        private CarritoResponseDTO convertirACarritoResponseDTO(Carrito carrito) {
                List<CarritoItemDTO> itemsDTO = carrito.getItems().stream()
                                .map(item -> {
                                        Producto p = item.getProducto();
                                        BigDecimal subtotal = p.getPreciodesct()
                                                        .multiply(BigDecimal.valueOf(item.getCantidad()));
                                        return CarritoItemDTO.builder()
                                                        .iddetalle(item.getIddetalle())
                                                        .productoid(p.getIdproducto())
                                                        .nombre(p.getNombre())
                                                        .marca(p.getMarca())
                                                        .imgurl(p.getImgurl())
                                                        .preciodesct(p.getPreciodesct())
                                                        .cantidad(item.getCantidad())
                                                        .subtotal(subtotal)
                                                        .build();
                                })
                                .collect(Collectors.toList());

                BigDecimal total = itemsDTO.stream()
                                .map(CarritoItemDTO::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return CarritoResponseDTO.builder()
                                .idcarrito(carrito.getIdcarrito())
                                .fechacreacion(carrito.getFechacreacion())
                                .items(itemsDTO)
                                .total(total)
                                .build();
        }
}