package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {
    // Obtiene el pago de un pedido (null si el pedido aún no fue pagado)
    Optional<Pago> findByPedido_Idpedido(UUID pedidoid);
}
