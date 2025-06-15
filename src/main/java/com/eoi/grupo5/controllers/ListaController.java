package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.*;
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
    private final SupermercadoService supermercadoService;


    public ListaController(UsuarioService usuarioService, ListaService listaService, ItemListaService itemListaService, ProductoService productoService, CarritoService carritoService, SupermercadoService supermercadoService) {
        this.usuarioService = usuarioService;
        this.listaService = listaService;
        this.itemListaService = itemListaService;
        this.productoService = productoService;
        this.carritoService = carritoService;
        this.supermercadoService = supermercadoService;
    }


    /**
     * Muestra la página con las listas guardadas del usuario
     * @param userDetails - Detalles del usuario autentificado
     * @param model - Modelo
     * @return Página con las listas guardadas del usuario
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

    /**
     * Visualiza una de las listas guardadas del usuario
     * @param id - ID de la lista guardada a visualizar
     * @param userDetails - Detalles del usuario autentificado
     * @param model - Modelo
     * @return Datos de la lista seleccionada
     */
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

    /**
     * Añade un producto a la lista abierta del usuario
     * @param productoId - ID del producto a añadir
     * @param userDetails - Detalles del usaurio autentificado
     * @param model - Modelo
     * @return Visualiza la lista abierta (carrito) con todos los productos añadidos a la lista
     */
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

        List<Supermercado> supermercados = supermercadoService.findAll();

        model.addAttribute("supermercados", supermercados);
        model.addAttribute("listaAbierta", listaAbierta);
        model.addAttribute("itemsListaAbierta", itemsListaAbierta);

        return "redirect:/listas/listaAbierta";

    }

    /**
     * Visualiza la lista abierta (carrito) del usuario
     * @param userDetails - Detalles del usuario
     * @param model - Modelo
     * @return Visualización de la lista abierta del usuario con los productos añadidos a la lista
     */
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

        List<Supermercado> supermercados = supermercadoService.findAll();

        model.addAttribute("supermercados", supermercados);
        model.addAttribute("listaAbierta", listaAbierta);
        model.addAttribute("itemsListaAbierta", itemsListaAbierta);

        return "listaAbierta";

    }

    /**
     * Cierra la lista abierta, crea una nueva lista abierta y muestra la página de listas guardadas del usuario
     * @param userDetails - Detalles del usuario
     * @param redirectAttributes - Atributos redirigidos (muestra un mensaje si se redirigió la lista con éxito)
     * @param model - Modelo
     * @return Visualización de las listas guardadas del usuario con un mensaje de éxito al cerrar la lista abierta
     */
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
     * Aumenta la cantidad de un producto de la lista abierta en 1
     * @param itemId - ID del item cuya cantidad se aumenta
     * @return Vuelve a visualizar la lista abierta
     */
    @GetMapping("/listas/incrementarCantidad")
    public String incrementarCantidad(@RequestParam Long itemId) {
        carritoService.incrementarCantidadItem(itemId);
        return "redirect:/listas/listaAbierta";
    }

    /**
     * Disminuye la cantidad de un producto de la lista abierta en 1.
     * Si la cantidad se queda a 0, se elimina el producto de la lista.
     * @param itemId - ID del item cuya cantidad se aumenta
     * @return Vuelve a visualizar la lista abierta
     */
    @GetMapping("/listas/disminuirCantidad")
    public String disminuirCantidad(@RequestParam Long itemId) {
        carritoService.disminuirCantidadItem(itemId);
        return "redirect:/listas/listaAbierta";
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
