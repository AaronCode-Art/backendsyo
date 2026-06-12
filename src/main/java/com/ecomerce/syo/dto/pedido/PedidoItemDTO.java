package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

/** Producto comprado dentro del detalle de un pedido. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemDTO {
    private UUID iddetalle;
    private UUID productoid;
    private String nombreProducto;
    private String imgurl;
    private Integer cantidad;
    private BigDecimal preciounitario;
    private BigDecimal descuento;
    private BigDecimal preciototal;
}
