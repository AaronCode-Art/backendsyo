package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.producto.ProductoDetalleDTO;
import com.ecomerce.syo.dto.producto.ProductoListaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductoService {

    /**
     * Devuelve el detalle completo de un producto
     */
    ProductoDetalleDTO obtenerDetalle(UUID id);

    /**
     * Devuelve todos los productos con paginación
     */
    Page<ProductoListaDTO> listarTodos(Pageable pageable);

    /**
     * Devuelve los productos de una categoría específica (el más importante para tu frontend)
     */
    Page<ProductoListaDTO> listarPorCategoria(UUID categoriaId, Pageable pageable);
    /**
     * Nuevo método con filtros avanzados
     * Se usa en la página de productos con filtros de categoría y rango de precio
     */
    Page<ProductoListaDTO> buscarConFiltros( UUID categoriaId, BigDecimal precioMin, BigDecimal precioMax, Pageable pageable );
}