package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.ItemLista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemListaRepository extends JpaRepository<ItemLista, Long> {


}
