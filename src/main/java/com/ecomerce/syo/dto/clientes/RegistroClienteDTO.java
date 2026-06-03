package com.ecomerce.syo.dto.clientes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroClienteDTO {

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

    private String contrasena;

}
