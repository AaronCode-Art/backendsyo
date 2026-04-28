package com.ecomerce.syo.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para mostrar la LISTA de productos
 * Solo contiene la información básica que quieres mostrar en la tienda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoListaDTO {
private UUID idproducto;
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal preciodesct;
    private Integer stock;
    private String imgurl;
}