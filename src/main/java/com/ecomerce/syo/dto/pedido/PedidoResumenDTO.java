// ─── PedidoResumenDTO.java ────────────────────────────────────────────────────
package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Lista de pedidos del cliente (GET /api/pedidos/mis-pedidos) */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PedidoResumenDTO {
    private UUID idpedido;
    private String nroticket;
    private OffsetDateTime fecha;
    private String estado;
    private String tipoentrega;
    private String tipocomprobante;
    private BigDecimal total;
    private int cantidadItems;
}
