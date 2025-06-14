package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemListaRepository extends JpaRepository<ItemLista, Long> {
    Optional<ItemLista> findById(Long id);
    Optional<List<ItemLista>> findAllByLista(Lista lista);


}
