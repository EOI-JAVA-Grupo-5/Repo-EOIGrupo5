package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.utils.exceptions.ItemsListaNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CarritoService {

    private final ListaService listaService;
    private final ItemListaService itemListaService;

    public CarritoService(ListaService listaService, ItemListaService itemListaService) {
        this.listaService = listaService;
        this.itemListaService = itemListaService;
    }

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

    public void incrementarCantidadItem(Long idItem){
        ItemLista item = itemListaService.findById(idItem)
                .orElseThrow(()->new ItemsListaNotFoundException("Item no encontrado"));
        item.setCantidadComprada(item.getCantidadComprada()+1);
        itemListaService.save(item);
    }

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
