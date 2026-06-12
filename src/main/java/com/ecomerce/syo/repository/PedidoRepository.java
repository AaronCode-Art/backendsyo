// ─── PedidoRepository.java ───────────────────────────────────────────────────
package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    // Todos los pedidos del cliente ordenados del más reciente
    List<Pedido> findAllByCliente_IdclienteOrderByFechaDesc(UUID clienteid);

    // Pedidos del cliente filtrados por nombre de estado
    @Query("SELECT p FROM Pedido p WHERE p.cliente.idcliente = :clienteid AND p.estado.nombre = :estado ORDER BY p.fecha DESC")
    List<Pedido> findByClienteAndEstado(@Param("clienteid") UUID clienteid, @Param("estado") String estado);
}
