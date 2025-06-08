package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // 🔹 Obtener productos paginados y filtrados
    public Page<Producto> getProductosFiltrados(String categoria, String supermercado, int page, int size, String orden) {
        Sort sort = Sort.by("price");
        if ("desc".equalsIgnoreCase(orden)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return productoRepository.findByFiltros(
                categoria == null || categoria.isEmpty() ? null : categoria,
                supermercado == null || supermercado.isEmpty() ? null : supermercado,
                pageable
        );
    }

    public List<String> getCategoryDisponibles() {
        return productoRepository.findDistinctCategories();
    }

    public List<String> getSupermercadosDisponibles() {
        return productoRepository.findDistinctSupermarkets();
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Page<Producto> findBySupermercado(String nombreSupermercado, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombreSupermercado.toLowerCase(), pageable);
    }

    public Page<Producto> productoSupermercadoByNameAsc(Pageable pageable){
        return productoRepository.findAllByOrderByNameAsc(pageable);
    }

    public Page<Producto> productoSupermercadoByNameDesc(Pageable pageable){
        return productoRepository.findAllByOrderByNameDesc(pageable);
    }

    public Page<Producto> productoSupermercadoByPriceAsc(Pageable pageable){
        return productoRepository.findAllByOrderByPriceAsc(pageable);
    }

    public Page<Producto> productoSupermercadoByPriceDesc(Pageable pageable){
        return productoRepository.findAllByOrderByPriceDesc(pageable);
    }
}

