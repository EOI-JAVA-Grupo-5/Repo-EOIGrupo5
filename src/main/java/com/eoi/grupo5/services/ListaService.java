package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ListaRepository;
import com.eoi.grupo5.utils.exceptions.ListaNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ListaService {

    private final ListaRepository listaRepository;

    public ListaService(ListaRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    /**
     * Guarda la lista en la BBDD
     * @param lista - Lista a guardar
     * @return Guarda la lista
     */
    public Lista save(Lista lista) {
        return listaRepository.save(lista);
    }

    /**
     * Borra la lista de la BBDD
     * @param lista - Lista a borrar
     */
    public void delete(Lista lista) {
         listaRepository.delete(lista);
    }

    /**
     * Obtiene una lista con todas las listas del usuario (incluida la lista abierta [carrito])
     * @param usuario - Usuario del cual se buscan las listas
     * @return Optional de las listas buscadas
     */
    public Optional<List<Lista>> getListasDeUsuario(Usuario usuario) {
        return listaRepository.findAllByUsuario(usuario);
    }

    /**
     * Devuelve una lista basada en su ID y su usuario
     * @param idLista - ID de la lista
     * @param usuario - Usuario
     * @return Lista si la encuentra o null si no
     */
    public Lista getLista(Long idLista, Usuario usuario){
        Lista lista = listaRepository.findListaById(idLista)
                .orElseThrow(() -> new ListaNotFoundException("No se encontró la lista nº" + idLista));
        if(lista.getUsuario().getId() == usuario.getId()){
            return lista;
        }else{
            return null;
        }
    }

    /**
     * Obtiene la lista abierta (carrito) de un usuario
     * @param usuario - Usuario
     * @return Lista abierta (carrito) del usuario
     */
    public Lista getListaAbierta(Usuario usuario){
        Optional <List<Lista>> listasUsuario = getListasDeUsuario(usuario);
        Lista listaAbierta = new Lista();

        if(listasUsuario.isPresent()) {
            List<Lista> listas = listasUsuario.get();

            for (int i = 0; i < listas.size(); i++) {
                if (!listas.get(i).isCerrada()) {
                    listaAbierta = listas.get(i);
                    i = listas.size();
                }
            }
        }

        return listaAbierta;
    }

    /**
     * Añade un item a la lista abierta (carrito)
     * @param itemLista - Item a añadir
     * @param usuario - Usuario
     */
    public void addItemListaALista(ItemLista itemLista, Usuario usuario){
        Lista listaAbierta = getListaAbierta(usuario);
        itemLista.setLista(listaAbierta);
    }

    /**
     * Crea una nueva lista abierta para el usuario
     * @param usuario - Usuario
     */
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
