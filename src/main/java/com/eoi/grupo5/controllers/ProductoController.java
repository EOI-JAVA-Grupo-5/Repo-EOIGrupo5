package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/paginaDeProducto")
    public String listarProductos(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supermarket,
            @RequestParam(required = false) String orden,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int pageSize = 5;

        Page<Producto> productos = productoService.buscarConFiltros(category, supermarket, orden, page, pageSize);

        // Productos paginados
        model.addAttribute("productos", productos.getContent());
        model.addAttribute("hasMore", productos.hasNext());
        model.addAttribute("paginaActual", page);

        // Filtros disponibles
        List<String> categorias = productoService.obtenerCategory();
        List<String> supermercados = productoService.obtenerSupermercados();

        model.addAttribute("categorias", categorias);
        model.addAttribute("supermercados", supermercados);

        // Valores seleccionados (para mantenerlos en el formulario)
        model.addAttribute("categoriaSeleccionada", category);
        model.addAttribute("supermercadoSeleccionado", supermarket);
        model.addAttribute("orden", orden);

        return "paginaDeProducto"; // HTML en templates/
    }

    // 🔵 Página de detalle (si decides mantenerla en el futuro)
    @GetMapping("/producto/{id}")
    public String mostrarDetalleProducto(@PathVariable Long id, Model model) {
        return productoService.obtenerProductoPorId(id)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    return "producto/detalle";
                })
                .orElse("redirect:/paginaDeProducto");
    }
}
