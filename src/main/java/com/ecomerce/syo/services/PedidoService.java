package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.pedido.ConfirmarPedidoDTO;
import com.ecomerce.syo.dto.pedido.PedidoDetalleDTO;
import com.ecomerce.syo.dto.pedido.PedidoResumenDTO;

import java.util.List;
import java.util.UUID;

/**
 * PedidoService - Operaciones de pedidos.
 *
 * confirmarPedido  → lee el carrito, crea el pedido+detalles+pago, vacía el carrito
 * misPedidos       → todos los pedidos del cliente autenticado
 * obtenerDetalle   → detalle completo de un pedido (items + historial + pago)
 * pedidosPorEstado → filtrar pedidos del cliente por estado
 */
public interface PedidoService {
    PedidoDetalleDTO confirmarPedido(ConfirmarPedidoDTO dto, String correoAutenticado);
    List<PedidoResumenDTO> misPedidos(String correoAutenticado);
    PedidoDetalleDTO obtenerDetalle(UUID pedidoid, String correoAutenticado);
    List<PedidoResumenDTO> pedidosPorEstado(String estado, String correoAutenticado);
}
