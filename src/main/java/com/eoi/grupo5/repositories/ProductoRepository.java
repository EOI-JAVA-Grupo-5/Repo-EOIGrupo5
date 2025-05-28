package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
