package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Pago - Registro del pago de un pedido.
 *
 * estadopago:  "pendiente" | "completado" | "fallido" | "reembolsado"  (CHECK en BD)
 * metodopago:  "tarjeta_credito" | "tarjeta_debito" | "yape" | "plin"
 *            | "transferencia" | "efectivo"                             (CHECK en BD)
 *
 * Se crea cuando el cliente confirma el pago en el checkout.
 */
@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idpago", updatable = false, nullable = false)
    private UUID idpago;

    // Pedido al que corresponde (RESTRICT → no se puede eliminar un pedido con pago)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoid", nullable = false)
    private Pedido pedido;

    @CreationTimestamp
    @Column(name = "fechapago", nullable = false, updatable = false)
    private OffsetDateTime fechapago;

    // "pendiente" | "completado" | "fallido" | "reembolsado"
    @Column(name = "estadopago", nullable = false, length = 20)
    private String estadopago;

    // "tarjeta_credito" | "tarjeta_debito" | "yape" | "plin" | "transferencia" | "efectivo"
    @Column(name = "metodopago", nullable = false, length = 30)
    private String metodopago;
}
