package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.categoria.CategoriaDetalleDTO;
import com.ecomerce.syo.dto.categoria.CategoriaListaDTO;
import com.ecomerce.syo.execption.ResourceNotFoundException;
import com.ecomerce.syo.model.Categoria;
import com.ecomerce.syo.repository.CategoriaRepository;
import com.ecomerce.syo.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaListaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::mapToListaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaDetalleDTO obtenerPorId(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        return mapToDetalleDTO(categoria);
    }

    private CategoriaListaDTO mapToListaDTO(Categoria c) {
        return CategoriaListaDTO.builder()
                .idcategoria(c.getIdcategoria())
                .nombre(c.getNombre())
                .imgurl(c.getImgurl())
                .build();
    }

    private CategoriaDetalleDTO mapToDetalleDTO(Categoria c) {
        return CategoriaDetalleDTO.builder()
                .idcategoria(c.getIdcategoria())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .imgurl(c.getImgurl())
                .build();
    }
}