package com.ecomerce.syo.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO completo de una categoría (si en el futuro quieres mostrar más información)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDetalleDTO {

    private UUID idcategoria;
    private String nombre;
    private String descripcion;
    private String imgurl;
}