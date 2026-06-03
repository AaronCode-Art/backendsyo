package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    // ====================== PEDIDOS ======================
    @Query("SELECT p FROM Pedido p WHERE p.cliente.idcliente = :clienteId")
    List<Pedido> findByClienteId(@Param("clienteId") UUID clienteId);

    @Query("SELECT p FROM Pedido p WHERE p.estado.idestado = :estadoId")
    List<Pedido> findByEstadoId(@Param("estadoId") UUID estadoId);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles WHERE p.idpedido = :id")
    Optional<Pedido> findByIdWithDetalles(@Param("id") UUID id);

    // ====================== DETALLE PEDIDO ======================
    @Query("SELECT d FROM DetallePedido d WHERE d.pedido.idpedido = :pedidoId")
    List<DetallePedido> findDetalleByPedidoId(@Param("pedidoId") UUID pedidoId);

    // ====================== HISTORIAL ======================
    @Query("SELECT h FROM HistorialEstadoPedido h WHERE h.pedido.idpedido = :pedidoId ORDER BY h.fecha ASC")
    List<HistorialEstadoPedido> findHistorialByPedidoId(@Param("pedidoId") UUID pedidoId);

    // ====================== PAGOS ======================
    @Query("SELECT p FROM Pago p WHERE p.pedido.idpedido = :pedidoId")
    List<Pago> findByPedidoId(@Param("pedidoId") UUID pedidoId);

    // ====================== ESTADOS ======================
    @Query("SELECT e FROM EstadoPedido e WHERE e.nombre = :nombre")
    Optional<EstadoPedido> findEstadoByNombre(@Param("nombre") String nombre);

    @Query("SELECT e FROM EstadoPedido e WHERE e.idestado = :id")
    Optional<EstadoPedido> findEstadoById(@Param("id") UUID id);

    // ====================== CONTEO ======================
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.idcliente = :clienteId")
    long countByClienteId(@Param("clienteId") UUID clienteId);
}