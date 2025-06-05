package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<String> obtenerCategory() {
        return productoRepository.findDistinctCategories();
    }

    public List<String> obtenerSupermercados() {
        return productoRepository.findDistinctSupermarkets();
    }

    public Page<Producto> buscarConFiltros(String category, String supermarket, String orden, int page, int size) {
        Pageable pageable;

        if ("precio_asc".equalsIgnoreCase(orden)) {
            pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        } else if ("precio_desc".equalsIgnoreCase(orden)) {
            pageable = PageRequest.of(page, size, Sort.by("price").descending());
        } else {
            pageable = PageRequest.of(page, size);
        }

        return productoRepository.findByFiltros(
                (category != null && !category.isBlank()) ? category : null,
                (supermarket != null && !supermarket.isBlank()) ? supermarket : null,
                pageable
        );
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> findBySupermercado(String nombreSupermercado) {
        return productoRepository.findBySupermarketIgnoreCase(nombreSupermercado.toLowerCase());
    }

}
