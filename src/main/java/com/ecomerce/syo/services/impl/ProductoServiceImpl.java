package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.producto.ProductoDetalleDTO;
import com.ecomerce.syo.dto.producto.ProductoListaDTO;
import com.ecomerce.syo.model.Producto;
import com.ecomerce.syo.repository.ProductoRepository;
import com.ecomerce.syo.services.ProductoService;
import com.ecomerce.syo.execption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementación del servicio de productos
 * Aquí está la lógica principal de la aplicación
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoDetalleDTO obtenerDetalle(UUID id) {
        Producto producto = productoRepository.findByIdWithCategoria(id)
                .orElseThrow(() -> 
                    new ResourceNotFoundException("Producto no encontrado con id: " + id));

        return mapToDetalleDTO(producto);
    }

    @Override
    public Page<ProductoListaDTO> listarTodos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(this::mapToListaDTO);
    }

    @Override
    public Page<ProductoListaDTO> listarPorCategoria(UUID categoriaId, Pageable pageable) {
        return productoRepository.findByCategoria_Idcategoria(categoriaId, pageable)
                .map(this::mapToListaDTO);
    }

    // Mapeo de Producto a ProductoListaDTO (solo campos básicos)
    private ProductoListaDTO mapToListaDTO(Producto p) {
        return ProductoListaDTO.builder()
                .idproducto(p.getIdproducto())
                .nombre(p.getNombre())
                .marca(p.getMarca())
                .precio(p.getPrecio())
                .descuento(p.getDescuento())
                .preciodesct(p.getPreciodesct())
                .stock(p.getStock())
                .imgurl(p.getImgurl())
                .build();
    }

    // Mapeo de Producto a ProductoDetalleDTO (todos los campos)
    private ProductoDetalleDTO mapToDetalleDTO(Producto p) {
        return ProductoDetalleDTO.builder()
                .idproducto(p.getIdproducto())
                .nombre(p.getNombre())
                .marca(p.getMarca())
                .precio(p.getPrecio())
                .preciodesct(p.getPreciodesct())
                .descuento(p.getDescuento())
                .stock(p.getStock())
                .descripcion(p.getDescripcion())
                .especificacionestecnicas(p.getEspecificacionestecnicas())
                .imgurl(p.getImgurl())
                .categoriaNombre(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                .build();
    }
}