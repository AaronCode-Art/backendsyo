package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Información del pago asociado al pedido. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoPagoDTO {
    private UUID idpago;
    private OffsetDateTime fechapago;
    private String estadopago;
    private String metodopago;
}
