package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * EstadoPedido - Catálogo de estados del pedido (datos fijos en BD).
 *
 * Estados disponibles (seeded en Neon):
 *   Pendiente        → pedido creado, no pagado
 *   Pagado           → pago registrado
 *   En preparacion   → tienda alistando el pedido
 *   Enviado          → pedido en camino
 *   Entregado        → cliente recibió el pedido
 *   Cancelado        → pedido cancelado
 *
 * NOTA: El estado en BD históricamente tenía el typo "Rntregado".
 * Ya fue corregido a "Entregado" en la BD, el código usa "Entregado".
 */
@Entity
@Table(name = "estadospedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idestado", updatable = false, nullable = false)
    private UUID idestado;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
}
