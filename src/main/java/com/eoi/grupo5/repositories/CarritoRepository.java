package com.eoi.grupo5.repositories;


import com.eoi.grupo5.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Puedes añadir métodos de consulta personalizados aquí si los necesitas,
    // por ejemplo, findByUsuarioId(Long usuarioId) si los carritos están asociados a usuarios.
}