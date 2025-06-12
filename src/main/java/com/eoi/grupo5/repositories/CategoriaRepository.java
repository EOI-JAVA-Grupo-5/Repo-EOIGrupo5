package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
