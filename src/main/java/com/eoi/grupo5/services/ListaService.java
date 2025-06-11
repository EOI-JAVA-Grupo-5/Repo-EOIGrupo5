package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ListaRepository;
import com.eoi.grupo5.utils.exceptions.ListaNotFoundException;
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

    public Lista getLista(Long idLista, Usuario usuario){
        Lista lista = listaRepository.findListaById(idLista)
                .orElseThrow(() -> new ListaNotFoundException("No se encontró la lista nº" + idLista));
        if(lista.getUsuario().getId() == usuario.getId()){
            return lista;
        }else{
            return null;
        }
    }
}
