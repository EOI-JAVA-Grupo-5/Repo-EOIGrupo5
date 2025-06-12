package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
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

        public void ajustarCuentasLista(Lista lista){
        Optional<List<ItemLista>> itemsOptional = itemListaService.getItemsDeLista(lista);

        if (itemsOptional.isPresent()){
            List<ItemLista> items = itemsOptional.get();

            BigDecimal cuentaCosteTotal = BigDecimal.valueOf(0);

            if(!items.isEmpty()){

                for (ItemLista item : items){
                    cuentaCosteTotal = cuentaCosteTotal.add(BigDecimal.valueOf(item.getProducto().getPrice()));
                }

            }

            lista.setCosteTotal(cuentaCosteTotal);
            listaService.save(lista);
        }
    }
}
