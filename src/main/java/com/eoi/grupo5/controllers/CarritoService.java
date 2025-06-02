package com.eoi.grupo5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CarritoService {

    @Autowired
    private CarritoService carritoService;

    private String obtenerUsuarioActual() {
        return "usuario123"; // reemplaza con lógica de usuario real
    }

    @GetMapping("/vista")
    public String vistaCarrito(ModelMap model) {
        String usuarioId = obtenerUsuarioActual();
        List<CarritoItem> items = carritoService.obtenerItemsDeCarrito(usuarioId);

        model.addAttribute("listacarrito", items);
        model.addAttribute("total", items.stream().mapToDouble(CarritoItem::getSubtotal).sum());

        return "carrito/vista";
    }

    @GetMapping("/add")
    public String mostrarFormularioAgregar(ModelMap model) {
        model.addAttribute("carritouno", new CarritoItem());
        return "carrito/nuevo";
    }

    @PostMapping("/add")
    public String agregarItem(@ModelAttribute("carritouno") CarritoItem item) {
        carritoService.agregarItemAlCarrito(obtenerUsuarioActual(), item);
        return "redirect:/carrito/vista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return "redirect:/carrito/vista";
    }
}
