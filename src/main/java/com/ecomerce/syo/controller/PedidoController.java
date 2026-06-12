package com.ecomerce.syo.controller;

import com.ecomerce.syo.dto.pedido.*;
import com.ecomerce.syo.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * PedidoController - Gestión de pedidos.
 * Todos los endpoints son protegidos (requieren JWT).
 *
 * POST /api/pedidos/confirmar          → confirmar compra desde el carrito
 * GET  /api/pedidos/mis-pedidos        → todos mis pedidos
 * GET  /api/pedidos/{id}               → detalle completo de un pedido
 * GET  /api/pedidos/estado?nombre=...  → mis pedidos filtrados por estado
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    private String correo() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Confirmar compra: lee el carrito, crea el pedido, procesa el pago, vacía el carrito.
     * Devuelve el detalle completo con ticket, historial y pago.
     */
    @PostMapping("/confirmar")
    public ResponseEntity<PedidoDetalleDTO> confirmar(@RequestBody ConfirmarPedidoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.confirmarPedido(dto, correo()));
                
    }

    /** Todos los pedidos del cliente autenticado, del más reciente al más antiguo */
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResumenDTO>> misPedidos() {
        return ResponseEntity.ok(pedidoService.misPedidos(correo()));
    }

    /** Detalle completo: items + historial de estados + pago + dirección */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalleDTO> detalle(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.obtenerDetalle(id, correo()));
    }

    /**
     * Pedidos filtrados por estado.
     * Ejemplo: GET /api/pedidos/estado?nombre=En preparacion
     * Estados válidos: Pendiente | Pagado | En preparacion | Enviado | Entregado | Cancelado
     */
    @GetMapping("/estado")
    public ResponseEntity<List<PedidoResumenDTO>> porEstado(@RequestParam String nombre) {
        return ResponseEntity.ok(pedidoService.pedidosPorEstado(nombre, correo()));
    }
}
