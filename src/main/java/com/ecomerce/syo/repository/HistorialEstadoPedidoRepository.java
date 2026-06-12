package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.HistorialEstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistorialEstadoPedidoRepository extends JpaRepository<HistorialEstadoPedido, UUID> {
    // Historial ordenado cronológicamente ASC (para mostrar el timeline)
    List<HistorialEstadoPedido> findAllByPedido_IdpedidoOrderByFechaAsc(UUID pedidoid);
}
