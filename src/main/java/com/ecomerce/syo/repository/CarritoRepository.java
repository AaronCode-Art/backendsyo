package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CarritoRepository extends JpaRepository<Carrito, UUID> {

    /**
     * Busca el carrito de un cliente por su ID
     */
    @Query("SELECT c FROM Carrito c WHERE c.cliente.idcliente = :clienteId")
    Optional<Carrito> findByClienteId(@Param("clienteId") UUID clienteId);

    /**
     * Verifica si existe un carrito para el cliente
     */
    @Query("SELECT COUNT(c) > 0 FROM Carrito c WHERE c.cliente.idcliente = :clienteId")
    boolean existsByClienteId(@Param("clienteId") UUID clienteId);

    /**
     * Busca el carrito con todos sus items (evita problemas N+1)
     */
    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.cliente.idcliente = :clienteId")
    Optional<Carrito> findByClienteIdWithItems(@Param("clienteId") UUID clienteId);

    
}