package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DetallePedido - Línea de producto dentro de un pedido.
 *
 * preciounitario y descuento son snapshots del momento de la compra.
 * Si el producto cambia de precio después, el historial no se altera.
 *
 * iddireccion → FK a direccionpedido (solo aplica si tipoentrega = "delivery").
 *               Si es "recojo" este campo es null.
 */
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

    // Producto comprado (RESTRICT → no se puede eliminar un producto con pedidos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productoid", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    // Precio al momento de la compra (snapshot)
    @Column(name = "preciounitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal preciounitario;

    // Descuento al momento de la compra (snapshot, default 0)
    @Column(name = "descuento", nullable = false, precision = 5, scale = 2)
    private BigDecimal descuento;

    // preciounitario * cantidad * (1 - descuento/100)
    @Column(name = "preciototal", nullable = false, precision = 12, scale = 2)
    private BigDecimal preciototal;

    // Dirección de entrega snapshot (null si tipoentrega = "recojo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddireccion", nullable = true)
    private DireccionPedido direccion;
}
