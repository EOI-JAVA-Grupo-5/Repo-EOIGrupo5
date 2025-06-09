package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ListaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaService {

    private ListaRepository listaRepository;

    public ListaService(ListaRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    public Lista save(Lista lista) {
        return listaRepository.save(lista);
    }

    public void delete(Lista lista) {
         listaRepository.delete(lista);
    }

    public Optional<List<Lista>> getListasDeUsuario(Usuario usuario) {
        return listaRepository.findAllByUsuario(usuario);
    }
}
