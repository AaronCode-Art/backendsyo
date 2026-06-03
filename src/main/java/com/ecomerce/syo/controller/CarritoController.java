package com.ecomerce.syo.controller;

import com.ecomerce.syo.dto.carrito.CarritoResponseDTO;
import com.ecomerce.syo.services.CarritoService;
import com.ecomerce.syo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import com.ecomerce.syo.model.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CarritoController - Controlador del carrito de compras
 * 
 * Permite a los usuarios autenticados:
 * - Ver su carrito
 * - Agregar productos
 * - Cambiar cantidad
 * - Eliminar productos
 * - Vaciar el carrito
 */
@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final ClienteRepository clienteRepository;

    /**
     * Obtiene el carrito completo del usuario logueado
     * GET http://localhost:8080/api/carrito
     */
    @GetMapping
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito() {
        UUID clienteId = obtenerClienteIdActual();
        CarritoResponseDTO carrito = carritoService.obtenerCarrito(clienteId);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Agrega un producto al carrito (o aumenta la cantidad si ya existe)
     * POST http://localhost:8080/api/carrito/agregar?productoId=xxx&cantidad=1
     */
    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponseDTO> agregarProducto(
            @RequestParam UUID productoId,
            @RequestParam(defaultValue = "1") Integer cantidad) {

        UUID clienteId = obtenerClienteIdActual();
        CarritoResponseDTO carrito = carritoService.agregarProducto(clienteId, productoId, cantidad);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Cambia la cantidad de un producto en el carrito
     * PUT http://localhost:8080/api/carrito/cantidad?productoId=xxx&cantidad=3
     */
    @PutMapping("/cantidad")
    public ResponseEntity<CarritoResponseDTO> cambiarCantidad(
            @RequestParam UUID productoId,
            @RequestParam Integer cantidad) {

        UUID clienteId = obtenerClienteIdActual();
        CarritoResponseDTO carrito = carritoService.cambiarCantidad(clienteId, productoId, cantidad);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Elimina un producto específico del carrito
     * DELETE http://localhost:8080/api/carrito/{productoId}
     */
    @DeleteMapping("/{productoId}")
    public ResponseEntity<CarritoResponseDTO> eliminarProducto(@PathVariable UUID productoId) {
        UUID clienteId = obtenerClienteIdActual();
        CarritoResponseDTO carrito = carritoService.eliminarProducto(clienteId, productoId);
        return ResponseEntity.ok(carrito);
    }

    /**
     * Vacía todo el carrito
     * DELETE http://localhost:8080/api/carrito
     */
    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito() {
        UUID clienteId = obtenerClienteIdActual();
        carritoService.vaciarCarrito(clienteId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el ID del cliente actual a partir del token JWT
     */
    private UUID obtenerClienteIdActual() {

    var auth = SecurityContextHolder
            .getContext()
            .getAuthentication();

    if (
        auth == null ||
        !auth.isAuthenticated() ||
        auth.getName().equals("anonymousUser")
    ) {

        throw new AuthenticationCredentialsNotFoundException(
            "Usuario no autenticado"
        );
    }

    String email = auth.getName();

    return clienteRepository
            .findByCorreo(email)
            .map(Cliente::getIdcliente)
            .orElseThrow(() ->
                new RuntimeException(
                    "Cliente no encontrado con email: " + email
                )
            );
}
}