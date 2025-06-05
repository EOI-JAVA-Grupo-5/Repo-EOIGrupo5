package com.eoi.grupo5.controllers;


import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Supermercado;
import com.eoi.grupo5.services.ProductoService;
import com.eoi.grupo5.services.ProductoSupermercadoService;
import com.eoi.grupo5.services.SupermercadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        System.out.println("SupermercadoController - Inicio listarSupermercados");

        List<Supermercado> supermercados = supermercadoService.findAll();

        // Add debug logging
        System.out.println("SupermercadoController - Supermercados encontrados: " +
                (supermercados != null ? supermercados.size() : "null"));
        if (supermercados != null && !supermercados.isEmpty()) {
            supermercados.forEach(s -> System.out.println("Supermercado: " + s.getNombre()));
        }

        model.addAttribute("supermercados", supermercados);
        return "supermercados";
    }

    @GetMapping("/supermercados/{id}")
    public String verProductosDeSupermercado(@PathVariable Long id, Model model) {
        Supermercado supermercado = supermercadoService.findById(id);
        // Add debug logging
        System.out.println("Supermercado encontrado: " +
                (supermercado != null ? supermercado.getNombre() : "null"));

        List<Producto> productos = productoService.findBySupermercado(supermercado.getNombre());

        System.out.println("Productos encontrados para " + supermercado.getNombre() + ": " +
                (productos != null ? productos.size() : "null"));

        model.addAttribute("supermercado", supermercado);
        model.addAttribute("productos", productos);

        return "perfilSupermercado";
    }

}









