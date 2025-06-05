package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySupermarketIgnoreCase(String supermarket);
}
