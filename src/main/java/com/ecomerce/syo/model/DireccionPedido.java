package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * DireccionPedido - Snapshot de la dirección de entrega al momento de hacer el pedido.
 * Se guarda por separado para que cambios futuros en el perfil del cliente
 * no afecten pedidos ya realizados.
 * Referenciada desde DetallePedido.iddireccion (puede ser null si es recojo en tienda).
 */
@Entity
@Table(name = "direccionpedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "iddireccion", updatable = false, nullable = false)
    private UUID iddireccion;

    @Column(name = "direccion", nullable = false, columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "referencia", columnDefinition = "TEXT")
    private String referencia;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "codigopostal", length = 10)
    private String codigopostal;
}
