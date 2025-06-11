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

    // ✅ Productos destacados para la página de inicio
    public List<Producto> getProductosDestacados() {
        return productoRepository.findTop4ByOrderByPriceAsc();
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

    // 🔹 Obtener todos (por si los necesitas para otras funciones)
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }



    public Page<Producto> findBySupermercado(String nombreSupermercado, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombreSupermercado.toLowerCase(), pageable);
    }

    public Page<Producto> productoSupermercadoByNameAsc(Pageable pageable, String nombre){
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombre,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").ascending()));
    }

    public Page<Producto> productoSupermercadoByNameDesc(Pageable pageable, String nombre){
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombre,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").descending()));
    }

    public Page<Producto> productoSupermercadoByPriceAsc(Pageable pageable, String nombre){
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombre,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending()));
    }

    public Page<Producto> productoSupermercadoByPriceDesc(Pageable pageable, String nombre){
        return productoRepository.findBySupermarketIgnoreCaseContaining(nombre,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending()));
    }

    public Page<Producto> findBySupermercadoAndCategoria(String nombre, String categoria, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContainingAndCategory(nombre, categoria, pageable);
    }

    public Page<Producto> productoSupermercadoByNameAscAndCategoria(String nombre, String categoria, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContainingAndCategory(nombre, categoria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").ascending()));
    }

    public Page<Producto> productoSupermercadoByNameDescAndCategoria(String nombre, String categoria, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContainingAndCategory(nombre, categoria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").descending()));
    }

    public Page<Producto> productoSupermercadoByPriceAscAndCategoria(String nombre, String categoria, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContainingAndCategory(nombre, categoria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending()));
    }

    public Page<Producto> productoSupermercadoByPriceDescAndCategoria(String nombre, String categoria, Pageable pageable) {
        return productoRepository.findBySupermarketIgnoreCaseContainingAndCategory(nombre, categoria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending()));
    }
}


