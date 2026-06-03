package com.ecomerce.syo.controller;

import com.ecomerce.syo.dto.pedido.PedidoResponseDTO;
import com.ecomerce.syo.model.Cliente;
import com.ecomerce.syo.repository.ClienteRepository;
import com.ecomerce.syo.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * PedidoController - Versión AVANZADA (Recomendada)
 * Obtiene automáticamente el cliente logueado desde el JWT
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;

    /**
     * Crear pedido desde el carrito del usuario logueado
     * Ejemplo: POST /api/pedidos/crear?tipoEntrega=delivery&tipoComprobante=boleta
     */
    @PostMapping("/crear")
    public ResponseEntity<PedidoResponseDTO> crearPedidoDesdeCarrito(
            @RequestParam String tipoEntrega,
            @RequestParam String tipoComprobante) {

        String correoLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente cliente = clienteRepository.findByCorreo(correoLogueado)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        PedidoResponseDTO pedido = pedidoService.crearPedidoDesdeCarrito(
                cliente.getIdcliente(), tipoEntrega, tipoComprobante);

        return ResponseEntity.ok(pedido);
    }

    /**
     * Obtener TODOS los pedidos del usuario logueado
     * Ejemplo: GET /api/pedidos/mis-pedidos
     */
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerMisPedidos() {
        String correoLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente cliente = clienteRepository.findByCorreo(correoLogueado)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<PedidoResponseDTO> pedidos = pedidoService.obtenerPorCliente(cliente.getIdcliente());
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtener pedidos por estado (útil para admin o filtros)
     */
    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPorEstado(@PathVariable UUID estadoId) {
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerPorEstado(estadoId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Detalle completo de un pedido
     */
    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponseDTO> obtenerDetalle(@PathVariable UUID pedidoId) {
        PedidoResponseDTO pedido = pedidoService.obtenerDetalle(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Cambiar estado de un pedido
     */
    @PutMapping("/{pedidoId}/estado/{nuevoEstadoId}")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(
            @PathVariable UUID pedidoId,
            @PathVariable UUID nuevoEstadoId) {

        PedidoResponseDTO pedido = pedidoService.cambiarEstado(pedidoId, nuevoEstadoId);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Cancelar un pedido
     */
    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable UUID pedidoId) {
        PedidoResponseDTO pedido = pedidoService.cancelarPedido(pedidoId);
        return ResponseEntity.ok(pedido);
    }
}