package com.ecomerce.syo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idcliente", updatable = false, nullable = false)
    private UUID idcliente;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "numero", nullable = false, unique = true, length = 15)
    private String numero;

    @Column(name = "direccion", nullable = false, columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "referencia", columnDefinition = "TEXT")
    private String referencia;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "codigopostal", length = 10)
    private String codigopostal;

    @Column(name = "correo", nullable = false, unique = true, length = 255)
    private String correo;

    @Column(name = "contrasena", nullable = false, columnDefinition = "TEXT")
    private String contrasena;
}
