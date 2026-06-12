package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Pedido - Cabecera del pedido. Solo se crea cuando el cliente confirma la compra
 * desde el carrito (no antes).
 *
 * tipoentrega:     "delivery" | "recojo"     (CHECK en BD)
 * tipocomprobante: "boleta"   | "factura"    (CHECK en BD)
 * nroticket:       "S-XXXXX"  generado al crear el pedido
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idpedido", updatable = false, nullable = false)
    private UUID idpedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteid", nullable = false)
    private Cliente cliente;

    // Estado actual del pedido (FK a estadospedido)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estadoid", nullable = false)
    private EstadoPedido estado;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private OffsetDateTime fecha;

    // "delivery" o "recojo" (CHECK en BD)
    @Column(name = "tipoentrega", nullable = false, length = 10)
    private String tipoentrega;

    // "boleta" o "factura" (CHECK en BD)
    @Column(name = "tipocomprobante", nullable = false, length = 10)
    private String tipocomprobante;

    // Total final del pedido (suma de preciototal de cada detalle)
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    // Ticket visible al cliente: "S-XXXXX" (5 dígitos aleatorios)
    @Column(name = "nroticket", nullable = false, length = 10)
    private String nroticket;

    // Detalles del pedido (CASCADE ALL)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    // Historial cronológico de cambios de estado
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoPedido> historial;

    // Pago del pedido (puede ser null si aún está Pendiente)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Pago> pagos;
}
