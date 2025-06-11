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


    // 🔹 Filtro principal: categoría, supermercado y búsqueda parcial por nombre
    @Query("SELECT p FROM Producto p " +
            "WHERE (:category IS NULL OR LOWER(p.category) = LOWER(:category)) " +
            "AND (:supermarket IS NULL OR LOWER(p.supermarket) = LOWER(:supermarket)) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE %:name%)")
    Page<Producto> findByFiltros(@Param("category") String category,
                                 @Param("supermarket") String supermarket,
                                 @Param("name") String name,
                                 Pageable pageable);

    // 🔹 Categorías únicas
    @Query("SELECT DISTINCT p.category FROM Producto p")
    List<String> findDistinctCategories();

    // 🔹 Supermercados únicos
    @Query("SELECT DISTINCT p.supermarket FROM Producto p")
    List<String> findDistinctSupermarkets();

    // 🔹 Búsqueda libre por nombre del supermercado
    Page<Producto> findBySupermarketIgnoreCaseContaining(String supermarket, Pageable pageable);

    // 🔹 Productos destacados: los 4 más baratos
    List<Producto> findTop4ByOrderByPriceAsc();


//Métodos para filtrar por orden alfabético y precio
    Page<Producto> findAllByOrderByNameAsc(Pageable pageable);
    Page<Producto> findAllByOrderByNameDesc(Pageable pageable);
    Page<Producto> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Producto> findAllByOrderByPriceDesc(Pageable pageable);

    //Método para filtrar por categoría
    Page<Producto> findBySupermarketIgnoreCaseContainingAndCategory(String nombre, String categoria, Pageable pageable);

}



