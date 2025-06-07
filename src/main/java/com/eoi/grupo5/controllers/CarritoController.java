package com.eoi.grupo5.controllers;

import com.eoi.grupo5.services.CarritoService; // Importa tu CarritoService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService; // Inyecta el servicio del carrito

    @Autowired // Inyección de dependencia
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /**
     * Muestra la página principal del carrito.
     */
    @GetMapping("/total")
    public String verCarrito(Model model) {
        model.addAttribute("itemsCarrito", carritoService.obtenerItemsCarrito());
        model.addAttribute("totalUnidades", carritoService.calcularTotalUnidades());
        model.addAttribute("precioTotal", String.format("%.2f", carritoService.calcularPrecioTotal()));
        return "carrito_total";
    }

    /**
     * Añade un producto al carrito.
     */
    @PostMapping("/añadir")
    public String añadirProductoAlCarrito(@RequestParam("productoId") Long productoId,
                                          @RequestParam(value = "cantidad", defaultValue = "1") int cantidad,
                                          RedirectAttributes redirectAttrs) {
        if (carritoService.actualizarCantidad(productoId, cantidad)) {
            redirectAttrs.addFlashAttribute("mensaje", "Producto añadido/actualizado en el carrito.");
        } else {
            redirectAttrs.addFlashAttribute("error", "El producto no pudo ser añadido al carrito (no encontrado).");
        }
        return "redirect:/carrito/total";
    }

    /**
     * Actualiza la cantidad de un producto específico en el carrito.
     */
    @PostMapping("/actualizar")
    public String actualizarCantidadProducto(@RequestParam("productoId") Long productoId,
                                             @RequestParam("cantidad") int nuevaCantidad,
                                             RedirectAttributes redirectAttrs) {
        if (carritoService.actualizarCantidad(productoId, nuevaCantidad)) {
            redirectAttrs.addFlashAttribute("mensaje", "Cantidad del producto actualizada.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Producto no encontrado en el carrito para actualizar.");
        }
        return "redirect:/carrito/total";
    }

    /**
     * Elimina un producto del carrito.
     */
    @PostMapping("/eliminar")
    public String eliminarProductoDelCarrito(@RequestParam("productoId") Long productoId,
                                             RedirectAttributes redirectAttrs) {
        if (carritoService.eliminarProducto(productoId)) {
            redirectAttrs.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        } else {
            redirectAttrs.addFlashAttribute("error", "El producto no pudo ser eliminado (no encontrado en el carrito).");
        }
        return "redirect:/carrito/total";
    }

    /**
     * Vacía completamente el carrito.
     */
    @PostMapping("/vaciar")
    public String vaciarCarrito(RedirectAttributes redirectAttrs) {
        carritoService.vaciarCarrito();
        redirectAttrs.addFlashAttribute("mensaje", "El carrito ha sido vaciado.");
        return "redirect:/carrito/total";
    }
}