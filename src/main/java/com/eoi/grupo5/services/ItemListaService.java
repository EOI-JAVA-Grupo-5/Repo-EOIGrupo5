package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ItemListaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ItemListaService {

    private final ItemListaRepository itemListaRepository;
    private final ListaService listaService;

    public ItemListaService(ItemListaRepository itemListaRepository, ListaService listaService) {
        this.itemListaRepository = itemListaRepository;
        this.listaService = listaService;
    }

    /**
     * Busca un Item de lista en la BBDD
     * @param id - ID del item
     * @return Optional del item
     */
    public Optional<ItemLista> findById(Long id){
        return itemListaRepository.findById(id);
    }

    /**
     * Guarda el item en la BBDD
     * @param itemLista - Item a guardar
     * @return Guarda el item en la BBDD
     */
    public ItemLista save(ItemLista itemLista) {
        return itemListaRepository.save(itemLista);
    }

    /**
     * Borra el item de la BBDD
     * @param itemLista - Item a borrar
     */
    public void delete(ItemLista itemLista) {
        itemListaRepository.delete(itemLista);
    }

    /**
     * Obtiene todos los items de una lista
     * @param lista - Lista de la que se quiere obtener los items
     * @return Optional de lista con todos los items
     */
    public Optional<List<ItemLista>> getItemsDeLista (Lista lista) {
        return itemListaRepository.findAllByLista(lista);
    }


    /**
     * Método que toma un producto y lo añade a la lista abierta de un usuario.
     * Si al buscar la lista abierta, se ve el producto en sus items, le suma 1 a la cantidad.
     * Si no encuentra el producto en los items, añade un nuevo item para el producto con cantidad 1.
     * Si la lista de items está vacía(no hay items), añade un nuevo item para el producto con cantidad 1.
     *
     * @param producto - Producto a meter en la lista
     * @param usuario - Usuario al que se le añade el producto
     */
    public void addProductoALista(Producto producto, Usuario usuario){
        Lista listaAbierta = listaService.getListaAbierta(usuario);

        Optional<List<ItemLista>> optionalItemListas = getItemsDeLista(listaAbierta);

        if(optionalItemListas.isPresent()){
            List<ItemLista> items = optionalItemListas.get();
            boolean productoEncontrado = false;

            for (int i = 0; i < items.size(); i++){
                ItemLista itemActual = items.get(i);


                if(itemActual.getProducto().getId().equals(producto.getId())){
                    productoEncontrado = true;
                    itemActual.setCantidadComprada(itemActual.getCantidadComprada() + 1);
                    save(itemActual);
                    i = items.size();
                }
            }

            if (!productoEncontrado){
                ItemLista nuevoItem = new ItemLista();
                nuevoItem.setLista(listaAbierta);
                nuevoItem.setProducto(producto);
                nuevoItem.setCantidadComprada(1);
                save(nuevoItem);
            }
        }
        else{
            ItemLista nuevoItem = new ItemLista();
            nuevoItem.setLista(listaAbierta);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidadComprada(1);
            save(nuevoItem);
        }
    }

    /**
     * Método que organiza los items de una List de items
     * @param listaDesorganizada - Lista sin organizar
     * @return Lista organizada
     */
    public List<ItemLista> ordenarItems(List<ItemLista> listaDesorganizada){
        List<ItemLista> listaOrganizada = listaDesorganizada;
//        Collections.sort(listaOrganizada,
//                (i1, i2) -> i1.getProducto().getId().compareTo(i2.getProducto().getId()));
        listaOrganizada.sort(Comparator.comparing((ItemLista item) -> item.getProducto().getSupermarket())
                .thenComparing(item -> item.getProducto().getId()));

        return listaOrganizada;
    }
}
