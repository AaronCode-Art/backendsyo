package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoid", nullable = false)
    private Pedido pedido;

    @Column(name = "fechapago", nullable = false, updatable = false)
    private LocalDateTime fechapago = LocalDateTime.now();

    @Column(name = "estadopago", nullable = false, length = 20)
    private String estadopago;     // pendiente, completado, fallido, reembolsado

    @Column(name = "metodopago", nullable = false, length = 30)
    private String metodopago;     // yape, plin, tarjeta_credito, etc.
}