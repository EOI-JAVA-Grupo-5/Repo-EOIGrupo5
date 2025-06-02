package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/paginaDeProducto")
    public String mostrarProductos(Model model) {
        List<Producto> productos = productoService.getAllProductos();
        model.addAttribute("productos", productos);
        return "paginaDeProducto";
    }
}
