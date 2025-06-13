package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.ItemLista;
import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.services.*;
import com.eoi.grupo5.utils.exceptions.ItemsListaNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ListaController {

    private final UsuarioService usuarioService;
    private final ListaService listaService;
    private final ItemListaService itemListaService;
    private final ProductoService productoService;
    private final CarritoService carritoService;


    public ListaController(UsuarioService usuarioService, ListaService listaService, ItemListaService itemListaService, ProductoService productoService, CarritoService carritoService) {
        this.usuarioService = usuarioService;
        this.listaService = listaService;
        this.itemListaService = itemListaService;
        this.productoService = productoService;
        this.carritoService = carritoService;
    }





    /**
     * Muestra la página principal del carrito.
     */
    @GetMapping("/listas")
    public String verListas(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        List<Lista> listasUsuario = listaService.getListasDeUsuario(usuario).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        model.addAttribute("listasUsuario", listasUsuario);
        return "listas";
    }

    @GetMapping("/listas/verLista/{id}")
    public String verListaIndividual(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Lista lista = listaService.getLista(id, usuario);

        if (lista != null){
            List<ItemLista> itemsLista = itemListaService.getItemsDeLista(lista)
                    .orElseThrow(() -> new ItemsListaNotFoundException("No se encontraron items para la lista nº"+id.toString()));
            itemListaService.ordenarItems(itemsLista);
            model.addAttribute("itemsLista", itemsLista);
        }

        return "verLista";
    }

    @PostMapping("/listas/addProducto")
    public String agregarProductoComoItem(@RequestParam("productoId") Long productoId, @AuthenticationPrincipal UserDetails userDetails, Model model){
        Producto producto = productoService.getProductoPorId(productoId);

        String username = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        Lista listaAbierta = listaService.getListaAbierta(usuario);

        itemListaService.addProductoALista(producto, usuario);
        carritoService.ajustarCuentasLista(listaAbierta);
        listaService.save(listaAbierta);

        List<ItemLista> itemsListaAbierta = itemListaService.getItemsDeLista(listaAbierta)
                .orElseThrow(() -> new ItemsListaNotFoundException("No se encontraron items para la lista nº"+listaAbierta.getId().toString()));;
        itemListaService.ordenarItems(itemsListaAbierta);

        model.addAttribute("listaAbierta", listaAbierta);
        model.addAttribute("itemsListaAbierta", itemsListaAbierta);

        return "redirect:/listas/listaAbierta";

    }

    @GetMapping("/listas/listaAbierta")
    public String verCarrito(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Lista listaAbierta = listaService.getListaAbierta(usuario);
        carritoService.ajustarCuentasLista(listaAbierta);
        listaService.save(listaAbierta);

        List<ItemLista> itemsListaAbierta = itemListaService.getItemsDeLista(listaAbierta)
                .orElseThrow(() -> new ItemsListaNotFoundException("No se encontraron items para la lista nº"+listaAbierta.getId().toString()));;
        itemListaService.ordenarItems(itemsListaAbierta);

        model.addAttribute("listaAbierta", listaAbierta);
        model.addAttribute("itemsListaAbierta", itemsListaAbierta);

        return "listaAbierta";

    }

    @PostMapping("/listas/listaCerrada")
    public String cerrarLista(@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes, Model model){
        String username = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Lista listaAbierta = listaService.getListaAbierta(usuario);
        listaAbierta.setCerrada(true);
        listaAbierta.setFecha(new Date());
        listaService.save(listaAbierta);
        listaService.crearListaParaUsuario(usuario);

        redirectAttributes.addFlashAttribute("seCerroLista", true);

        return "redirect:/listas";
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
