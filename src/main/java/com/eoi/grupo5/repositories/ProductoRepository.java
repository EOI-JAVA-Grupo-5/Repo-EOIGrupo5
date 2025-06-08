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

    // 🔹 Para el filtro por supermercado individual
    List<Producto> findBySupermarketIgnoreCaseContaining(String supermarket);
    Page<Producto> findBySupermarketIgnoreCaseContaining(String supermarket, Pageable pageable);

    // 🔹 Para obtener categorías únicas
    @Query("SELECT DISTINCT p.category FROM Producto p")
    List<String> findDistinctCategories();

    // 🔹 Para obtener supermercados únicos
    @Query("SELECT DISTINCT p.supermarket FROM Producto p")
    List<String> findDistinctSupermarkets();

    // 🔹 Para obtener productos con filtros, paginación y orden
    @Query("SELECT p FROM Producto p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:supermarket IS NULL OR p.supermarket = :supermarket)")
    Page<Producto> findByFiltros(@Param("category") String category,
                                 @Param("supermarket") String supermarket,
                                 Pageable pageable);

    //Métodos para filtrar por orden alfabético y precio
    Page<Producto> findAllByOrderByNameAsc(Pageable pageable);
    Page<Producto> findAllByOrderByNameDesc(Pageable pageable);
    Page<Producto> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Producto> findAllByOrderByPriceDesc(Pageable pageable);
}



