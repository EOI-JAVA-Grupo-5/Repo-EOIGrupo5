package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.utils.exceptions.ItemsListaNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final ListaService listaService;
    private final ItemListaService itemListaService;

    public CarritoService(ListaService listaService, ItemListaService itemListaService) {
        this.listaService = listaService;
        this.itemListaService = itemListaService;
    }

    /**
     * Ajusta el coste total de una lista
     * @param lista - Lista a ajustar
     */
    public void ajustarCuentasLista(Lista lista){
        Optional<List<ItemLista>> itemsOptional = itemListaService.getItemsDeLista(lista);

        if (itemsOptional.isPresent()){
            List<ItemLista> items = itemsOptional.get();

            BigDecimal cuentaCosteTotal = BigDecimal.valueOf(0);
            if(!items.isEmpty()){

                for (ItemLista item : items){
                    cuentaCosteTotal = cuentaCosteTotal.add(BigDecimal.valueOf(item.getProducto().getPrice()*item.getCantidadComprada()));
                }

            }

            lista.setCosteTotal(cuentaCosteTotal);
            listaService.save(lista);
        }
    }

    /**
     * Incrementa la cantidad comprada de un item de lista en 1
     * @param idItem - Item a aumentar
     */
    public void incrementarCantidadItem(Long idItem){
        ItemLista item = itemListaService.findById(idItem)
                .orElseThrow(()->new ItemsListaNotFoundException("Item no encontrado"));
        item.setCantidadComprada(item.getCantidadComprada()+1);
        itemListaService.save(item);
    }

    /**
     * Disminuye la cantidad comprada de un item de lista en 1.
     * Si la cantidad llega a 0, borra el item.
     * @param idItem - Item a disminuir
     */
    public void disminuirCantidadItem(Long idItem){
        ItemLista item = itemListaService.findById(idItem)
                .orElseThrow(()->new ItemsListaNotFoundException("Item no encontrado"));
        item.setCantidadComprada(item.getCantidadComprada()-1);

        if(item.getCantidadComprada() == 0){
            itemListaService.delete(item);
        }else{
            itemListaService.save(item);
        }
    }
}
