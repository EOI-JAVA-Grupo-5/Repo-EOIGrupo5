package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Lista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Integer> {

}
