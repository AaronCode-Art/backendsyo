package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.time.OffsetDateTime;

/** Un cambio de estado del pedido con su fecha. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoHistorialDTO {
    private String estado;
    private OffsetDateTime fecha;
}
