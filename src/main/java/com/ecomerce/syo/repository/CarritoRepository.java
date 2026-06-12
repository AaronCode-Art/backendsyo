// ─── CarritoRepository.java ──────────────────────────────────────────────────
package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, UUID> {

    // Busca el carrito del cliente cargando también los items y sus productos (evita N+1)
    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.producto WHERE c.cliente.idcliente = :clienteid")
    Optional<Carrito> findByClienteIdWithItems(@Param("clienteid") UUID clienteid);

    // Solo para saber si el cliente ya tiene carrito
    Optional<Carrito> findByCliente_Idcliente(UUID clienteid);



}
