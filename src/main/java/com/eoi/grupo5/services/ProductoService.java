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

        // Determinar el orden ascendente o descendente por precio
        if ("desc".equalsIgnoreCase(orden)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        // Sustituir cadenas vacías por null para evitar filtros incorrectos
        String categoriaFiltrada = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
        String supermercadoFiltrado = (supermercado == null || supermercado.trim().isEmpty()) ? null : supermercado;

        return productoRepository.findByFiltros(categoriaFiltrada, supermercadoFiltrado, pageable);
    }

    // 🔹 Obtener todas las categorías disponibles
    public List<String> getCategoryDisponibles() {
        return productoRepository.findDistinctCategories();
    }

    // 🔹 Obtener todos los supermercados disponibles
    public List<String> getSupermercadosDisponibles() {
        return productoRepository.findDistinctSupermarkets();
    }

    // 🔹 Obtener todos los productos sin paginación
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    // 🔹 Obtener un producto por su ID
    public Producto getProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // 🔹 Obtener productos por nombre del supermercado (ignorando mayúsculas)
    public List<Producto> findBySupermercado(String nombreSupermercado) {
        return productoRepository.findBySupermarketIgnoreCase(nombreSupermercado.toLowerCase());
    }
}

