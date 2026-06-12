package com.ecomerce.syo.dto.clientes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO que se devuelve después de un login exitoso
 * Contiene el token JWT y los datos básicos del cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private UUID idcliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String numero;
    private String direccion;
    private String referencia;
    private String distrito;
    private String codigopostal;
    private String correo;
}