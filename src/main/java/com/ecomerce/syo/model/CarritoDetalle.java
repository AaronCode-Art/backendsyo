package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "carritodetalle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "iddetalle")
    private UUID iddetalle;

    @ManyToOne
    @JoinColumn(name = "carritoid", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "productoid", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;
}