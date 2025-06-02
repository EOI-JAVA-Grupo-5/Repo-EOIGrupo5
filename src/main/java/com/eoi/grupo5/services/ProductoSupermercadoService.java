package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.ProductoSupermercado;
import com.eoi.grupo5.repositories.ProductoSupermercadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoSupermercadoService {
    private final ProductoSupermercadoRepository productoSupermercadoRepository;

    public ProductoSupermercadoService(ProductoSupermercadoRepository productoSupermercadoRepository) { this.productoSupermercadoRepository = productoSupermercadoRepository; }

    public List<ProductoSupermercado> findAll() {
        return productoSupermercadoRepository.findAll();
    }

    public Optional<ProductoSupermercado> findById(Long id) {
        return productoSupermercadoRepository.findById(id);
    }

    public ProductoSupermercado save(ProductoSupermercado productoSupermercado) {
        return productoSupermercadoRepository.save(productoSupermercado);
    }

    public void deleteById(Long id) {
        productoSupermercadoRepository.deleteById(id);
    }

    public Object findBySupermercadoId(Long id) {
        return productoSupermercadoRepository.findBySupermercadoId(id);
    }
}
