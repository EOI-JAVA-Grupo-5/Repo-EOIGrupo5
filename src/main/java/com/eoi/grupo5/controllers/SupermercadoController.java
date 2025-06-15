package com.eoi.grupo5.controllers;


import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Supermercado;
import com.eoi.grupo5.services.ProductoService;
import com.eoi.grupo5.services.SupermercadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para gestionar la visualización de supermercados y sus productos,
 * así como la funcionalidad de agregar productos al carrito.
 */
@Controller
public class SupermercadoController {

    private final SupermercadoService supermercadoService;
    private final ProductoService productoService;

    /**
     * Constructor que inyecta los servicios necesarios.
     *
     * @param supermercadoService Servicio para manejar supermercados
     * @param productoService Servicio para manejar productos
     */
    public SupermercadoController(SupermercadoService supermercadoService, ProductoService productoService) {
        this.supermercadoService = supermercadoService;
        this.productoService = productoService;
    }

    /**
     * Muestra el listado de todos los supermercados.
     *
     * @param model Modelo de vista para Thymeleaf
     * @return Vista con la lista de supermercados
     */
    @GetMapping("/supermercados")
    public String listarSupermercados(Model model) {
       List<Supermercado> supermercados = supermercadoService.findAll();
        model.addAttribute("supermercados", supermercados);
        return "supermercados";
    }

    /**
     * Muestra los productos de un supermercado específico, con opciones de paginación, ordenación y filtrado por categoría.
     *
     * @param id id del supermercado
     * @param model Modelo para la vista
     * @param page Página actual de productos
     * @param orden Criterio de ordenación (asc, desc, precio_asc, precio_desc)
     * @param categoria Categoría para filtrar productos
     * @return Vista con los productos del supermercado
     */
    @GetMapping("/supermercados/{id}")
    public String verProductosDeSupermercado(@PathVariable Long id,
                                             Model model,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(required = false) String orden,
                                             @RequestParam(required = false) String categoria) {

        Supermercado supermercado = supermercadoService.findById(id);
        Pageable pageable = PageRequest.of(page, 20);

        Page<Producto> productosPage;

        // Filtrado y ordenación
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
                case "asc" -> productoService.productoSupermercadoByNameAsc(pageable, supermercado.getNombre());
                case "desc" -> productoService.productoSupermercadoByNameDesc(pageable, supermercado.getNombre());
                case "precio_asc" -> productoService.productoSupermercadoByPriceAsc(pageable, supermercado.getNombre());
                case "precio_desc" -> productoService.productoSupermercadoByPriceDesc(pageable, supermercado.getNombre());
                case null, default -> productoService.findBySupermercado(supermercado.getNombre(), pageable);
            };
        }

        // Obtener categorías únicas
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


    /**
     * Agrega un producto al carrito almacenado en sesión.
     *
     * @param productoId id del producto a agregar
     * @param cantidad Cantidad del producto a agregar
     * @param supermercadoId id del supermercado desde el cual se agrega
     * @param session Sesión HTTP donde se guarda el carrito
     * @return Redirección a la vista del supermercado correspondiente
     */
    @PostMapping("/Carrito/agregarProducto")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId,
                                   @RequestParam("cantidad") int cantidad,
                                   @RequestParam("supermercadoId") Long supermercadoId,
                                   HttpSession session) {
        Producto producto = productoService.getProductoPorId(productoId);
        if (producto != null) {
            List<Producto> carrito = (List<Producto>) session.getAttribute("Carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }

            for (int i = 0; i < cantidad; i++) {
                carrito.add(producto);
            }

            session.setAttribute("Carrito", carrito);
        }

        return "redirect:/supermercado/" + supermercadoId;
    }



}









