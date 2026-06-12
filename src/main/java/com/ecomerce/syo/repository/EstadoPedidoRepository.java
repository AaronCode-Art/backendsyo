// ─── EstadoPedidoRepository.java ─────────────────────────────────────────────
package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, UUID> {
    // Busca estado por nombre exacto (ej. "Pendiente", "Pagado")
    Optional<EstadoPedido> findByNombre(String nombre);
}
