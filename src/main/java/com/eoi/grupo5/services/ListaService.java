package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ListaRepository;
import com.eoi.grupo5.utils.exceptions.ListaNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ListaService {

    private final ListaRepository listaRepository;

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

    public void crearListaParaUsuario(Usuario usuario){
        Optional <List<Lista>> listasUsuario = getListasDeUsuario(usuario);

        if(listasUsuario.isPresent()){
            List<Lista> listas = listasUsuario.get();
            boolean hayListaAbierta = false;

            for(int i = 0; i< listas.size(); i++){
                if(!listas.get(i).isCerrada()){
                    hayListaAbierta = true;
                    i=listas.size();
                }
            }

            if(!hayListaAbierta){
                Lista nuevaLista = new Lista();
                nuevaLista.setUsuario(usuario);
                nuevaLista.setCerrada(false);
                nuevaLista.setCosteTotal(BigDecimal.valueOf(0.0));
                nuevaLista.setDineroAhorrado(BigDecimal.valueOf(0.0));

                save(nuevaLista);
            }
        }
    }
}
