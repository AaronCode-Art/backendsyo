package com.ecomerce.syo.services;

import com.ecomerce.syo.dto.clientes.LoguinClienteDTO;
import com.ecomerce.syo.dto.clientes.PerfilClienteDTO;
import com.ecomerce.syo.dto.clientes.RegistroClienteDTO;
import com.ecomerce.syo.dto.clientes.UpdateClienteDTO;
import com.ecomerce.syo.model.Cliente;
import java.util.UUID;


public interface ClienteService {
    
    /** Registra un nuevo cliente con todas las validaciones */
    Cliente registrar(RegistroClienteDTO dto);

    /** Login con correo y contraseña */
    Cliente login(LoguinClienteDTO dto);

    /** mostrar perfil del cliente */
    PerfilClienteDTO obtenerPerfil(UUID idcliente);

    /** actualizar perfil del cliente */
    Cliente actualizarPerfil(UUID idcliente, UpdateClienteDTO dto);

    /** eliminar cuenta del cliente*/
    void eliminarCuenta(UUID idcliente);
}
