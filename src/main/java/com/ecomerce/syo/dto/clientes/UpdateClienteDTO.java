package com.ecomerce.syo.dto.clientes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClienteDTO {

    private String nombre;
    private String apellido;
    private String numero;
    private String direccion;
    private String referencia;
    private String distrito;
    private String codigopostal;
    private String correo;           // opcional cambiar correo
    private String contrasena;       // opcional cambiar contraseña
}