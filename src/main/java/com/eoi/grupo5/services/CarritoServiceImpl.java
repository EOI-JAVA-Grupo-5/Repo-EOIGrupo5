package com.eoi.grupo5.services;

import com.eoi.grupo5.repositories.CarritoItemRepository;
import com.eoi.grupo5.repositories.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepo;

    @Autowired
    private CarritoItemRepository carritoItemRepo;

    @Override
    public Carrito obtenerOCrearCarrito(String usuarioId) {
        return carritoRepo.findByUsuarioId(usuarioId)
                .orElseGet(() -> carritoRepo.save(new Carrito(usuarioId)));
    }

    @Override
    public void agregarItemAlCarrito(String usuarioId, CarritoItem item) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        carrito.agregarItem(item); // método en Carrito que asocia ambos lados
        carritoRepo.save(carrito);
    }

    @Override
    public List<CarritoItem> obtenerItemsDeCarrito(String usuarioId) {
        return obtenerOCrearCarrito(usuarioId).getItems();
    }

    @Override
    public void eliminarItem(Long itemId) {
        carritoItemRepo.deleteById(itemId);
    }
}
