// ─── CarritoItemResponseDTO.java ─────────────────────────────────────────────
package com.ecomerce.syo.dto.carrito;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Un item del carrito con los datos del producto necesarios para el frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemResponseDTO {
    private UUID iditem;
    private UUID productoid;
    private String nombre;
    private String marca;
    private String imgurl;
    private BigDecimal precioUnitario; // precio actual del producto (puede variar)
    private BigDecimal preciodesct; // precio con descuento actual
    private BigDecimal descuento;
    private Integer cantidad;
    private BigDecimal subtotal; // preciodesct * cantidad
    private Integer stockDisponible; // para mostrar si hay suficiente stock
}
