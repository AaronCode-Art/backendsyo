package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {

    private UUID idpedido;
    private String clienteNombre;
    private String estado;
    private LocalDateTime fecha;
    private String tipoEntrega;      // delivery o recojo
    private String tipoComprobante;  // boleta o factura
    private BigDecimal total;

    private List<DetallePedidoResponseDTO> detalles;
}