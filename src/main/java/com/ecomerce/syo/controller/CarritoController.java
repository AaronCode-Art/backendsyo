package com.ecomerce.syo.controller;

import com.ecomerce.syo.dto.carrito.*;
import com.ecomerce.syo.services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CarritoController - Gestión del carrito de compras.
 * Todos los endpoints son protegidos (requieren JWT).
 *
 * GET    /api/carrito              → obtener carrito del cliente
 * POST   /api/carrito/agregar      → agregar producto (upsert)
 * PUT    /api/carrito/item/{id}    → actualizar cantidad de un item
 * DELETE /api/carrito/item/{id}    → eliminar un item
 * DELETE /api/carrito              → vaciar todo el carrito
 */
@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    private String correo() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito() {
        return ResponseEntity.ok(carritoService.obtenerCarrito(correo()));
    }

    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponseDTO> agregar(@RequestBody AgregarCarritoDTO dto) {
        return ResponseEntity.ok(carritoService.agregarProducto(dto, correo()));
    }

    @PutMapping("/item/{itemid}")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
            @PathVariable UUID itemid,
            @RequestBody ActualizarItemDTO dto) {
        return ResponseEntity.ok(carritoService.actualizarItem(itemid, dto, correo()));
    }

    @DeleteMapping("/item/{itemid}")
    public ResponseEntity<CarritoResponseDTO> eliminarItem(@PathVariable UUID itemid) {
        return ResponseEntity.ok(carritoService.eliminarItem(itemid, correo()));
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciar() {
        carritoService.vaciar(correo());
        return ResponseEntity.noContent().build();
    }
}
