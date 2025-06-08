package com.eoi.grupo5.controllers;


import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Supermercado;
import com.eoi.grupo5.services.ProductoService;
import com.eoi.grupo5.services.SupermercadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class SupermercadoController {

    private final SupermercadoService supermercadoService;
    private final ProductoService productoService;

    public SupermercadoController(SupermercadoService supermercadoService, ProductoService productoService) {
        this.supermercadoService = supermercadoService;
        this.productoService = productoService;
    }

    @GetMapping("/supermercados")
    public String listarSupermercados(Model model) {
       List<Supermercado> supermercados = supermercadoService.findAll();
        model.addAttribute("supermercados", supermercados);
        return "supermercados";
    }

    @GetMapping("/supermercados/{id}")
    public String verProductosDeSupermercado(@PathVariable Long id,
                                             Model model,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(required = false) String orden,
                                             @RequestParam(required = false) String categoria) {

        Supermercado supermercado = supermercadoService.findById(id);
        Pageable pageable = PageRequest.of(page, 20);

        Page<Producto> productosPage;

        System.out.println("Filtrando por categoría: " + categoria);

        if (categoria != null && !categoria.isEmpty()) {
            productosPage = switch (orden) {
                case "asc" ->
                        productoService.productoSupermercadoByNameAscAndCategoria(supermercado.getNombre(), categoria, pageable);
                case "desc" ->
                        productoService.productoSupermercadoByNameDescAndCategoria(supermercado.getNombre(), categoria, pageable);
                case "precio_asc" ->
                        productoService.productoSupermercadoByPriceAscAndCategoria(supermercado.getNombre(), categoria, pageable);
                case "precio_desc" ->
                        productoService.productoSupermercadoByPriceDescAndCategoria(supermercado.getNombre(), categoria, pageable);
                case null, default ->
                        productoService.findBySupermercadoAndCategoria(supermercado.getNombre(), categoria, pageable);
            };
        } else {
            productosPage = switch (orden) {
                case "asc" -> productoService.productoSupermercadoByNameAsc(pageable);
                case "desc" -> productoService.productoSupermercadoByNameDesc(pageable);
                case "precio_asc" -> productoService.productoSupermercadoByPriceAsc(pageable);
                case "precio_desc" -> productoService.productoSupermercadoByPriceDesc(pageable);
                case null, default -> productoService.findBySupermercado(supermercado.getNombre(), pageable);
            };
        }


        List<Producto> todosLosProductos = productoService.findBySupermercado(supermercado.getNombre());
        Set<String> categorias = todosLosProductos.stream()
                .map(Producto::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        model.addAttribute("supermercado", supermercado);
        model.addAttribute("productosPage", productosPage);
        model.addAttribute("orden", orden);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categorias", categorias);

        return "perfilSupermercado";
    }


}









