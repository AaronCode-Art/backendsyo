package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "detallepedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "iddetalle", updatable = false, nullable = false)
    private UUID iddetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoid", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productoid", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "preciounitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal preciounitario;

    @Column(name = "descuento", nullable = false, precision = 5, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "preciototal", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciototal;
}