package com.ecomerce.syo.controller;


import com.ecomerce.syo.dto.producto.ProductoDetalleDTO;
import com.ecomerce.syo.dto.producto.ProductoListaDTO;
import com.ecomerce.syo.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Lista todos los productos (paginado)
    @GetMapping
    public ResponseEntity<Page<ProductoListaDTO>> listarTodos(
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(productoService.listarTodos(pageable));
    }

    // Detalle completo de un producto
    @GetMapping("/detalle/{id}")
    public ResponseEntity<ProductoDetalleDTO> obtenerDetalle(@PathVariable UUID id) {
        return ResponseEntity.ok(productoService.obtenerDetalle(id));
    }

    // Productos por categoría (el que más usarás en el frontend)
    @GetMapping("/categoria/{id}")
    public ResponseEntity<Page<ProductoListaDTO>> listarPorCategoria(
            @PathVariable UUID id,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(productoService.listarPorCategoria(id, pageable));
    }
}