package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, UUID> {
    List<DetallePedido> findAllByPedido_Idpedido(UUID pedidoid);
}
