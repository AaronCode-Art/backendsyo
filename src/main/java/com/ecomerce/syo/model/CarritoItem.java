package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * CarritoItem - Un producto dentro del carrito con su cantidad.
 * UNIQUE (carritoid, productoid) → el mismo producto no puede estar dos veces,
 * si el cliente agrega el mismo producto se actualiza la cantidad (upsert).
 * CHECK cantidad > 0 está garantizado en BD.
 */
@Entity
@Table(name = "carritoitems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "iditem", updatable = false, nullable = false)
    private UUID iditem;

    // Carrito al que pertenece este item (CASCADE desde Carrito)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carritoid", nullable = false)
    private Carrito carrito;

    // Producto agregado (RESTRICT → no se puede borrar un producto que está en algún carrito)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productoid", nullable = false)
    private Producto producto;

    // Cantidad de unidades (mínimo 1, garantizado por CHECK en BD)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
