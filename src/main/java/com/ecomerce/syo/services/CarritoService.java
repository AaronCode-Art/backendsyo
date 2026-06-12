package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.carrito.AgregarCarritoDTO;
import com.ecomerce.syo.dto.carrito.ActualizarItemDTO;
import com.ecomerce.syo.dto.carrito.CarritoResponseDTO;

import java.util.UUID;

/**
 * CarritoService - Operaciones del carrito de compras.
 *
 * obtenerCarrito   → devuelve el carrito del cliente (o uno vacío si no tiene)
 * agregarProducto  → agrega producto o suma cantidad si ya existe (upsert)
 * actualizarItem   → cambia la cantidad de un item (si cantidad=0 lo elimina)
 * eliminarItem     → elimina un item por su id
 * vaciar           → elimina todos los items del carrito
 */
public interface CarritoService {
    CarritoResponseDTO obtenerCarrito(String correoAutenticado);
    CarritoResponseDTO agregarProducto(AgregarCarritoDTO dto, String correoAutenticado);
    CarritoResponseDTO actualizarItem(UUID itemid, ActualizarItemDTO dto, String correoAutenticado);
    CarritoResponseDTO eliminarItem(UUID itemid, String correoAutenticado);
    void vaciar(String correoAutenticado);
}
