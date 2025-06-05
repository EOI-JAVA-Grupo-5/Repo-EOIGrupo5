package com.eoi.grupo5.controllers;


import com.eoi.grupo5.services.ProductoSupermercadoService;
import com.eoi.grupo5.services.SupermercadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SupermercadoController {

    private final SupermercadoService supermercadoService;
    private final ProductoSupermercadoService productoSupermercadoService;

    public SupermercadoController(SupermercadoService supermercadoService, ProductoSupermercadoService productoSupermercadoService) {
        this.supermercadoService = supermercadoService;
        this.productoSupermercadoService = productoSupermercadoService;
    }

    @GetMapping("/supermercados")
    public String listarSupermercados(Model model) {
        model.addAttribute("supermercados", supermercadoService.findAll());
        return "supermercados";
    }

    @GetMapping("/supermercados/{id}")
    public String verProductosDeSupermercado(@PathVariable Long id, Model model) {
        model.addAttribute("supermercado", supermercadoService.findById(id));
        model.addAttribute("productos", productoSupermercadoService.findBySupermercadoId(id));
        return "perfilSupermercado";
    }

}









