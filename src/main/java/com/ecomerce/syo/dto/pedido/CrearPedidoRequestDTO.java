package com.ecomerce.syo.dto.pedido;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearPedidoRequestDTO {
    private UUID clienteId;
    private String tipoEntrega;      // delivery o recojo
    private String tipoComprobante;  // boleta o factura
    private List<DetallePedidoDTO> detalles;
}