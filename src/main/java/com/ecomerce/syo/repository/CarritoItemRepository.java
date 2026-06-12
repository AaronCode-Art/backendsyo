package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, UUID> {

    // Busca un item específico dentro de un carrito por producto
    // Usado para el upsert: si existe → actualiza cantidad, si no → crea
    Optional<CarritoItem> findByCarrito_IdcarritoAndProducto_Idproducto(UUID carritoid, UUID productoid);

    // Todos los items de un carrito
    List<CarritoItem> findAllByCarrito_Idcarrito(UUID carritoid);

    // Eliminar todos los items de un carrito (vaciar al confirmar compra)
    @Transactional
    void deleteAllByCarrito_Idcarrito(UUID carritoid);
}
