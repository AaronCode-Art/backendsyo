package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByCorreo(String correo);
    Optional<Cliente> findByDni(String dni);
    Optional<Cliente> findByNumero(String numero);

    boolean existsByCorreo(String correo);
    boolean existsByDni(String dni);
    boolean existsByNumero(String numero);
}