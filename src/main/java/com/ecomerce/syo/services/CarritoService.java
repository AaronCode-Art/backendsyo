package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.carrito.CarritoResponseDTO;
import java.util.UUID;

public interface CarritoService {

    /**
     * Agrega un producto al carrito (o aumenta cantidad si ya existe)
     */
    CarritoResponseDTO agregarProducto(UUID clienteId, UUID productoId, Integer cantidad);

    /**
     * Cambia la cantidad de un producto en el carrito
     */
    CarritoResponseDTO cambiarCantidad(UUID clienteId, UUID productoId, Integer nuevaCantidad);

    /**
     * Elimina un producto del carrito
     */
    CarritoResponseDTO eliminarProducto(UUID clienteId, UUID productoId);

    /**
     * Obtiene el carrito completo del cliente
     */
    CarritoResponseDTO obtenerCarrito(UUID clienteId);

    /**
     * Vacía todo el carrito
     */
    void vaciarCarrito(UUID clienteId);
}