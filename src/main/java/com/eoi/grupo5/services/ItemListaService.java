package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ItemListaRepository;
import org.springframework.stereotype.Service;

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

    public ItemLista save(ItemLista itemLista) {
        return itemListaRepository.save(itemLista);
    }

    public void delete(ItemLista itemLista) {
        itemListaRepository.delete(itemLista);
    }

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
}
