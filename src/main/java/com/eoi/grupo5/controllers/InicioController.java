package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InicioController {

    private final ProductoService productoService;

    @Autowired
    public InicioController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping({"/", "/paginaDeInicio"})
    public String mostrarInicio(Model model) {
        List<Producto> destacados = productoService.getProductosDestacados();
        model.addAttribute("productosDestacados", destacados);
        return "paginaDeInicio";
    }
}
