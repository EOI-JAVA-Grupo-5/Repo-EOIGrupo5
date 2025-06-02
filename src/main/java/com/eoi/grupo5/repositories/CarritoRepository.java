package com.eoi.grupo5.repositories;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(String usuarioId);
}
