package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.RelacionUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelacionesUsuarioRepository extends JpaRepository<RelacionUsuarios, Long> {
}
