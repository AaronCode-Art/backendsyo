package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historialestadospedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialEstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idhistorial", updatable = false, nullable = false)
    private UUID idhistorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoid", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estadoid", nullable = false)
    private EstadoPedido estado;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}