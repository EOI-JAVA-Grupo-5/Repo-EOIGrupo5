package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.repositories.ItemListaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemListaService {

    private ItemListaRepository itemListaRepository;

    public ItemListaService(ItemListaRepository itemListaRepository) {
        this.itemListaRepository = itemListaRepository;
    }

    public ItemLista save(ItemLista itemLista) {
        return itemListaRepository.save(itemLista);
    }

    public void delete(ItemLista itemLista) {
        itemListaRepository.delete(itemLista);
    }

    public Optional<List<ItemLista>> getItemsDeLista (Lista lista) {
        return itemListaRepository.findAllByLista(lista);
    }
}
