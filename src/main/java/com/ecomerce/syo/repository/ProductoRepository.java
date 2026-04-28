package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    /**
     * Busca un producto por ID y carga su categoría (evita errores de Lazy Loading)
     */
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria WHERE p.idproducto = :id")
    Optional<Producto> findByIdWithCategoria(@Param("id") UUID id);

    /**
     * Busca todos los productos de una categoría específica
     * Este método es clave para tu frontend cuando el usuario selecciona una categoría
     */
    Page<Producto> findByCategoria_Idcategoria(UUID categoriaId, Pageable pageable);
}