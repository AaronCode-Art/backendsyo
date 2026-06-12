package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * HistorialEstadoPedido - Log cronológico de cada cambio de estado del pedido.
 * Cada vez que el estado cambia se inserta una nueva fila aquí con la fecha exacta.
 *
 * Flujo normal:
 *   Pendiente → Pagado → En preparacion → Enviado → Entregado
 *   Cualquier estado → Cancelado
 */
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

    // Estado registrado en este momento (RESTRICT → no se puede borrar un estado con historial)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estadoid", nullable = false)
    private EstadoPedido estado;

    // Fecha exacta del cambio de estado (automática)
    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private OffsetDateTime fecha;
}
