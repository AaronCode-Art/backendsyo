package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Carrito - Un cliente tiene exactamente un carrito (UNIQUE clienteid).
 * Se crea automáticamente la primera vez que el cliente agrega un producto.
 * Se vacía (eliminan los CarritoItems) cuando el cliente confirma el pago.
 * El carrito en sí NO se elimina — permanece vacío para la próxima compra.
 */
@Entity
@Table(name = "carrito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idcarrito", updatable = false, nullable = false)
    private UUID idcarrito;

    // Un cliente = un carrito (UNIQUE en BD)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteid", nullable = false, unique = true)
    private Cliente cliente;

    @CreationTimestamp
    @Column(name = "fechacreacion", nullable = false, updatable = false)
    private OffsetDateTime fechacreacion;

    // Items del carrito (CASCADE ALL → si se borra el carrito se borran los items)
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items;
}
