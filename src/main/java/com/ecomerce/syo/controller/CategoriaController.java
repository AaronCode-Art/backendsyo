package com.ecomerce.syo.controller;

import com.ecomerce.syo.dto.categoria.CategoriaDetalleDTO;
import com.ecomerce.syo.dto.categoria.CategoriaListaDTO;
import com.ecomerce.syo.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * GET /api/categorias
     * Devuelve todas las categorías (nombre + imgurl) → para los 3 cuadros del frontend
     */
    @GetMapping
    public ResponseEntity<List<CategoriaListaDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    /**
     * GET /api/categorias/{id}
     * Devuelve detalle completo de una categoría
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDetalleDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }
}