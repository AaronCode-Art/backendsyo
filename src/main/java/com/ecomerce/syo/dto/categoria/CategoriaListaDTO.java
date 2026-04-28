package com.ecomerce.syo.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO simple para mostrar lista de categorías (usado en los 3 cuadros del frontend)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaListaDTO {

    private UUID idcategoria;
    private String nombre;
    private String imgurl;
}