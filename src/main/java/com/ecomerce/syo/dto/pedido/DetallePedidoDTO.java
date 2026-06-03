package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoDTO {
    private UUID productoId;
    private Integer cantidad;
}