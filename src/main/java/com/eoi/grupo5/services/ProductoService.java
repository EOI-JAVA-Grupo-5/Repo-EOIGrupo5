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

    // ✅ Método principal con todos los filtros aplicados: categoría, supermercado, nombre y orden
    public Page<Producto> getProductosFiltrados(String category, String supermarket, String name, int page, int size, String orden) {
        Sort sort = Sort.by("price");
        if ("desc".equalsIgnoreCase(orden)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        String categoryFiltrada = (category == null || category.isBlank()) ? null : category;
        String supermercadoFiltrado = (supermarket == null || supermarket.isBlank()) ? null : supermarket;
        String nameFiltrado = (name == null || name.isBlank()) ? null : name.toLowerCase();

        return productoRepository.findByFiltros(categoryFiltrada, supermercadoFiltrado, nameFiltrado, pageable);
    }

    // 🔹 Categorías únicas
    public List<String> getCategoryDisponibles() {
        return productoRepository.findDistinctCategories();
    }

    // 🔹 Supermercados únicos
    public List<String> getSupermercadosDisponibles() {
        return productoRepository.findDistinctSupermarkets();
    }

    // 🔹 Buscar producto por ID (para el carrito)
    public Producto getProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // 🔹 Búsqueda libre por nombre de supermercado
    public Page<Producto> findBySupermercado(String nombreSupermercado, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombreSupermercado.toLowerCase(), pageable);
    }

    // 🔹 Obtener todos (por si los necesitas para otras funciones)
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
}

