package com.ecomerce.syo.dto.pedido;

import lombok.*;
import java.util.UUID;

/**
 * Dirección de entrega snapshot.
 * Input  → se usa en ConfirmarPedidoDTO (iddireccion será null, lo genera BD)
 * Output → se usa en PedidoDetalleDTO   (iddireccion viene relleno desde BD)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionPedidoDTO {
    private UUID iddireccion;
    private String direccion;
    private String referencia;
    private String distrito;
    private String codigopostal;
}
