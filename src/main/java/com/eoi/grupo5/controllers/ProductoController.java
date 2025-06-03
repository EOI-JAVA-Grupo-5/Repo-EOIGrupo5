package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // 🟢 Página principal: muestra todos los productos
    @GetMapping("/paginaDeProducto")
    public String mostrarTodosLosProductos(Model model) {
        List<Producto> productos = productoService.getAllProductos();
        model.addAttribute("productos", productos);
        return "paginaDeProducto"; // corresponde al archivo paginaDeProducto.html
    }

    // 🔵 Página de detalle: muestra un producto por ID
    @GetMapping("/producto/{id}")
    public String mostrarDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.getProductoPorId(id);
        if (producto != null) {
            model.addAttribute("producto", producto);
            return "producto/detalle"; // corresponde a producto/detalle.html
        } else {
            // si el producto no existe, redirige a la página principal
            return "redirect:/paginaDeProducto";
        }
    }
}
