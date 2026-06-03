package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoResponseDTO {

    private UUID productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal preciounitario;
    private BigDecimal preciototal;
}