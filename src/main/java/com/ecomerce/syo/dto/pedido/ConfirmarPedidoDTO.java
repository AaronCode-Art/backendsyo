package com.ecomerce.syo.dto.pedido;

import lombok.*;

/**
 * Body que envía el frontend al confirmar la compra desde el carrito.
 * POST /api/pedidos/confirmar
 *
 * El backend lee los items del carrito del cliente autenticado,
 * no se envían los productos por body (evita inconsistencias).
 *
 * tipoentrega:     "delivery" | "recojo"
 * tipocomprobante: "boleta"   | "factura"
 * metodopago:      "yape" | "plin" | "tarjeta_credito" | "tarjeta_debito"
 *                | "transferencia" | "efectivo"
 *
 * direccion: solo requerido si tipoentrega = "delivery", null si "recojo"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmarPedidoDTO {
    private String tipoentrega;
    private String tipocomprobante;
    private String metodopago;
    private DireccionPedidoDTO direccion;
}
