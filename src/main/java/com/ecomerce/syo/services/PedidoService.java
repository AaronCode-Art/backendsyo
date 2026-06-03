package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.pedido.PedidoResponseDTO;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de Pedidos - Versión final y limpia
 */
public interface PedidoService {

    /**
     * Crea un pedido desde el carrito del cliente
     */
    PedidoResponseDTO crearPedidoDesdeCarrito(UUID clienteId, String tipoEntrega, String tipoComprobante);

    /**
     * Lista todos los pedidos de un cliente
     */
    List<PedidoResponseDTO> obtenerPorCliente(UUID clienteId);

    /**
     * Lista los pedidos por estado (PENDIENTE, CONFIRMADO, ENVIADO, etc.)
     */
    List<PedidoResponseDTO> obtenerPorEstado(UUID estadoId);

    /**
     * Detalle completo de un pedido
     */
    PedidoResponseDTO obtenerDetalle(UUID pedidoId);

    /**
     * Cambia el estado de un pedido
     */
    PedidoResponseDTO cambiarEstado(UUID pedidoId, UUID nuevoEstadoId);

    /**
     * Cancela un pedido
     */
    PedidoResponseDTO cancelarPedido(UUID pedidoId);
}