package com.ecomerce.syo.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * DTO para mostrar el DETALLE completo de un producto
 * Se usa solo en el endpoint /detalle/{id}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDetalleDTO {

    private UUID idproducto;
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private BigDecimal preciodesct;
    private BigDecimal descuento;
    private Integer stock;
    private String descripcion;
    private Map<String, Object> especificacionestecnicas;
    private String imgurl;
    private String categoriaNombre;
}