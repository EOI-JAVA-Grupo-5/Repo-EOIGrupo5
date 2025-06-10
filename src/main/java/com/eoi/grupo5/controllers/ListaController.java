package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.services.ListaService;
import com.eoi.grupo5.services.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ListaController {

    private final UsuarioService usuarioService;
    private final ListaService listaService;


    public ListaController(UsuarioService usuarioService, ListaService listaService) {
        this.usuarioService = usuarioService;
        this.listaService = listaService;
    }





    /**
     * Muestra la página principal del carrito.
     */
    @GetMapping("/Listas")
    public String verListas(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//        List<Lista> listasUsuario = listaService.getListasDeUsuario(usuario).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//        model.addAttribute("listasUsuario", listasUsuario);
        return "listas";
    }

    /**
     * Añade un producto al carrito.
     */
//    @PostMapping("/añadir")
//    public String añadirProductoAlCarrito(@RequestParam("id") Long id,
//                                          @RequestParam("fecha") Date fecha,
//                                          @RequestParam("coste_total")BigDecimal costeTotal,
//                                          @RequestParam("dinero_Ahorrado") BigDecimal dineroAhorrado) {
//        if (id.(productoId, cantidad)) {
//            redirectAttrs.addFlashAttribute("mensaje", "Producto añadido/actualizado en el carrito.");
//        } else {
//            redirectAttrs.addFlashAttribute("error", "El producto no pudo ser añadido al carrito (no encontrado).");
//        }
//        return "redirect:/carrito/total";
//    }



}
