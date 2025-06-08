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
    public String verProductosDeSupermercado(@PathVariable Long id, Model model, @RequestParam(defaultValue = "0") int page) {
        Supermercado supermercado = supermercadoService.findById(id);

        Pageable pageable = PageRequest.of(page, 20);

        Page<Producto> productosPage = productoService.findBySupermercado(supermercado.getNombre(), pageable);

        model.addAttribute("supermercado", supermercado);
        model.addAttribute("productosPage", productosPage);

        return "perfilSupermercado";
    }

}









