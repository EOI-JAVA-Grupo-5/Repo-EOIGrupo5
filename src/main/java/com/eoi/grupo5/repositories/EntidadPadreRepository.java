//package com.eoi.grupo5.repositories;
//
//import com.eoi.grupo5.entities.EntidadPadre;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Collection;
//import java.util.Optional;
//
///**
// * Repository interface for managing {@link EntidadPadre} entities.
// * Extends {@link JpaRepository} to provide standard CRUD operations.
// * Custom query methods may be defined as needed.
// */
//@Repository
//public interface EntidadPadreRepository extends JpaRepository<EntidadPadre, Long> {
//    Optional<EntidadPadre> findByNombre(String jetBrains);
//
//    Collection<Object> findByNombreContaining(String padre);
//}