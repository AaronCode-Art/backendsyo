package com.ecomerce.syo.repository;

import com.ecomerce.syo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    // Por ahora no necesitamos métodos especiales
}