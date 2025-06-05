package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

<<<<<<< HEAD
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT DISTINCT p.category FROM Producto p")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT p.supermarket FROM Producto p")
    List<String> findDistinctSupermarkets();

    @Query("SELECT p FROM Producto p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:supermarket IS NULL OR p.supermarket = :supermarket)")
    Page<Producto> findByFiltros(@Param("category") String category,
                                 @Param("supermarket") String supermarket,
                                 Pageable pageable);
=======
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySupermarketIgnoreCase(String supermarket);
>>>>>>> 35d22f10dae7c0885c392240ae884fb8f945c63f
}
