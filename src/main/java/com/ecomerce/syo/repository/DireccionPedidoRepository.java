package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.DireccionPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DireccionPedidoRepository extends JpaRepository<DireccionPedido, UUID> {
    // Solo necesitamos save() y findById() — heredados de JpaRepository
}
