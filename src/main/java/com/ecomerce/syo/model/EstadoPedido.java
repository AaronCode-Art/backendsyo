package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

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

    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;   // PENDIENTE, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO, etc.
}