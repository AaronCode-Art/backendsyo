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
public class LoguinClienteDTO {
    private UUID idcliente;
    private String correo;
    private String contrasena;
   
}
