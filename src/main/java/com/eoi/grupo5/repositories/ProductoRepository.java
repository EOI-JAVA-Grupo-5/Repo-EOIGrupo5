package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // 🔹 Buscar productos por nombre de supermercado (ignorando mayúsculas)
    List<Producto> findBySupermarketIgnoreCase(String supermarket);

    // 🔹 Obtener todas las categorías únicas
    @Query("SELECT DISTINCT p.category FROM Producto p")
    List<String> findDistinctCategories();

    // 🔹 Obtener todos los supermercados únicos
    @Query("SELECT DISTINCT p.supermarket FROM Producto p")
    List<String> findDistinctSupermarkets();

    // 🔹 Buscar productos con filtros (permitiendo nulos o vacíos), paginación y orden
    @Query("SELECT p FROM Producto p WHERE " +
            "(:category IS NULL OR :category = '' OR p.category = :category) AND " +
            "(:supermarket IS NULL OR :supermarket = '' OR p.supermarket = :supermarket)")
    Page<Producto> findByFiltros(@Param("category") String category,
                                 @Param("supermarket") String supermarket,
                                 Pageable pageable);
}


