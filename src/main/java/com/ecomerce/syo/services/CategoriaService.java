package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.categoria.CategoriaDetalleDTO;
import com.ecomerce.syo.dto.categoria.CategoriaListaDTO;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {

    /**
     * Devuelve todas las categorías (para mostrar en los cuadros del frontend)
     */
    List<CategoriaListaDTO> listarTodas();

    /**
     * Devuelve el detalle completo de una categoría
     */
    CategoriaDetalleDTO obtenerPorId(UUID id);
}