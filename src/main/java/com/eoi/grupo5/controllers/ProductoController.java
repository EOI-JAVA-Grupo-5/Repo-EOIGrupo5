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

    // ✅ Página principal con filtros, búsqueda y paginación
    @GetMapping("/paginaDeProducto")
    public String mostrarProductos(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String supermarket,
            @RequestParam(defaultValue = "asc") String orden,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int pageSize = 15;

        // Llamamos al servicio con todos los filtros
        Page<Producto> productosPage = productoService.getProductosFiltrados(category, supermarket, name, page, pageSize, orden);

        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("pagina", page);
        model.addAttribute("hasMore", productosPage.hasNext());

        // Filtros disponibles
        model.addAttribute("category", productoService.getCategoryDisponibles());
        model.addAttribute("supermercados", productoService.getSupermercadosDisponibles());

        // Conservamos los filtros en el modelo para "Ver más"
        Map<String, String> parametros = new HashMap<>();
        parametros.put("category", category);
        parametros.put("supermarket", supermarket);
        parametros.put("orden", orden);
        parametros.put("name", name);
        model.addAttribute("param", parametros);

        return "paginaDeProducto";
    }

    // ✅ Añadir producto al carrito
    @PostMapping("/Carrito/agregar")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId, HttpSession session) {
        Producto producto = productoService.getProductoPorId(productoId);
        if (producto != null) {
            List<Producto> carrito = (List<Producto>) session.getAttribute("Carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }
            carrito.add(producto);
            session.setAttribute("Carrito", carrito);
        }
        return "redirect:/Carrito";
    }

    // ✅ Mostrar carrito
    @GetMapping("/Carrito")
    public String verCarrito(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("Carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        model.addAttribute("Carrito", carrito);
        return "Carrito";
    }
}
