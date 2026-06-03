package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "idcarrito")
    private UUID idcarrito;

    @OneToOne
    @JoinColumn(name = "clienteid", nullable = false, unique = true)
    private Cliente cliente;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechacreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoDetalle> items = new ArrayList<>();
}