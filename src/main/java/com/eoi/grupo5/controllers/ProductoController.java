package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.services.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // 🟢 Página principal: con filtros, orden y paginación
    @GetMapping("/paginaDeProducto")
    public String mostrarProductos(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supermarket,
            @RequestParam(defaultValue = "asc") String orden,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int pageSize = 5;

        Page<Producto> productosPage = productoService.getProductosFiltrados(category, supermarket, page, pageSize, orden);

        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("pagina", page);
        model.addAttribute("hasMore", productosPage.hasNext());

        model.addAttribute("category", productoService.getCategoryDisponibles());
        model.addAttribute("supermercados", productoService.getSupermercadosDisponibles());

        return "paginaDeProducto";
    }

    // 🔵 Añadir producto al carrito
    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId, HttpSession session) {
        Producto producto = productoService.getProductoPorId(productoId);
        if (producto != null) {
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }
            carrito.add(producto);
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/Carrito.html";
    }
}

