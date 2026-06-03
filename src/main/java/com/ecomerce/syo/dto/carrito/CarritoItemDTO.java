package com.ecomerce.syo.dto.carrito;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemDTO {
    private UUID iddetalle;
    private UUID productoid;
    private String nombre;
    private String marca;
    private String imgurl;
    private BigDecimal precio;
    private BigDecimal preciodesct;
    private Integer stock;
    private Integer cantidad;
    private BigDecimal subtotal;
}