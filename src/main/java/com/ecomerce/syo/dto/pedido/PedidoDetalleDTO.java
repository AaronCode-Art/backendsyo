package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Detalle completo de un pedido:
 * productos + historial de estados + pago + dirección de entrega.
 * GET /api/pedidos/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDetalleDTO {
    private UUID idpedido;
    private String nroticket;
    private OffsetDateTime fecha;
    private String estadoActual;
    private String tipoentrega;
    private String tipocomprobante;
    private BigDecimal total;
    private List<PedidoItemDTO> items;
    private List<PedidoHistorialDTO> historial;
    private PedidoPagoDTO pago;
    private DireccionPedidoDTO direccion; // null si tipoentrega = "recojo"
}
