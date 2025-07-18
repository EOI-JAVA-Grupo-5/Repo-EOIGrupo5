package com.eoi.grupo5.controllers;


import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.security.UsuarioDetailsService;
//import com.eoi.grupo5.services.EntidadHijaService;
//import com.eoi.grupo5.services.EntidadPadreService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Collection;

/**
 * Controlador encargado de manejar las solicitudes relacionadas con la entidad principal.
 *
 * Este controlador utiliza la anotación {@code @Controller} para ser detectado como un componente
 * Spring MVC y maneja solicitudes HTTP. Su objetivo principal es gestionar las operaciones
 * necesarias para mostrar una lista de entidades en la vista correspondiente.
 *
 * Anotaciones importantes:
 * - {@code @Controller}: Indica que esta clase se comporta como un controlador Spring MVC.
 * - {@code @PreAuthorize}: Define que el acceso a ciertos métodos esté restringido
 *   según las reglas de autorización establecidas.
 *
 * Dependencias:
 * - {@code EntidadPadreRepository}: Interfaz del repositorio que permite interactuar con
 *   la base de datos para operaciones de persistencia y consulta relacionadas con
 *   la entidad padre.
 *
 * Métodos principales:
 * - {@code listEntities}: Maneja solicitudes GET a la URL "/entities", recupera los
 *   datos de las entidades desde la base de datos y los pasa al modelo para mostrarlos
 *   en una vista.
 *
 */
@Controller
public class DefaultController {

//    private final EntidadHijaService entidadHijaService;
//    private final EntidadPadreService entidadPadreService;
//
//    /**
//     * Constructor de la clase DefaultController.
//     * <p>
//     * Inicializa el controlador principal asignando los servicios
//     * utilizados para gestionar las entidades EntidadPadre y EntidadHija.
//     *
//     * @param entidadHijaService  instancia de {@link EntidadHijaService} que proporciona
//     *                            funcionalidades adicionales relacionadas con la entidad EntidadHija.
//     * @param entidadPadreService instancia de {@link EntidadPadreService} que proporciona
//     *                            funcionalidades adicionales relacionadas con la entidad EntidadPadre.
//     */
//    public DefaultController(EntidadHijaService entidadHijaService, EntidadPadreService entidadPadreService) {
//        this.entidadHijaService = entidadHijaService;
//        this.entidadPadreService = entidadPadreService;
//    }

    /**
     * Método que lista las entidades disponibles y las añade al modelo para ser utilizadas en la vista.
     * Recupera todas las entidades de un repositorio y las presenta en una vista específica.
     *
     * @param model El objeto del modelo que se utiliza para compartir datos entre el backend y la vista.
     *              Aquí se añade un atributo llamado "entities" con la lista obtenida del repositorio.
     * @return Una cadena que representa el nombre de la vista ("entitiesList") donde se renderizarán las entidades.
     */
//    @GetMapping("/entities")
//    public String listEntities(Model model)
//    {
//        model.addAttribute("entidades", entidadHijaService.findAll());
//        return "entidadesHijas"; // View name
//    }


//    @GetMapping("/")
//    public String index(Model model)
//    {
//        return "paginaDeInicio"; // View name
//    }

    @GetMapping("/carrito")
    public String carrito(Model model)
    {
        return "carrito"; // View name
    }

//    @GetMapping("/inicioSesion")
//    public String login(Model model)
//    {
//        return "login"; // View name
//    }

//    @GetMapping("/registro")
//    public String registro(Model model)
//    {
//        return "register"; // View name
//    }

//    @GetMapping("/usuario")
//    public String usuario(Model model)
//    {
////        model.addAttribute("username", principal.getName());
////        return "perfilUsuario"; // View name
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            String username = userDetails.getUsername();         // nombre de usuario
//            String password = userDetails.getPassword();         // contraseña (generalmente cifrada)
//            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities(); // roles
//
//            // Si usas una clase propia (como Usuario), puedes hacer un cast
//            // y acceder a otros campos como correo, nombre real, etc.
//            if (userDetails instanceof UsuarioDetailsService) {
//                UsuarioDetailsService usuario = (UsuarioDetailsService) userDetails;
////                model.addAttribute("username", usuario.getUsername());
////                model.addAttribute("email", usuario.getCorreo());      // <- correo
////                model.addAttribute("nombre", usuario.getNombre());    // <- nombre real
//            } else {
//                model.addAttribute("username", userDetails.getUsername());
//            }
//        }
//
//        return "perfilUsuario";
//    }



//    @GetMapping("/supermercados")
//    public String perfilSupermercado(Model model)
//    {
//        return "supermercados"; // View name
//    }

//    @GetMapping("/paginaDeInicio")
//    public String paginaDeInicio(Principal principal, Model model)
//    {
//        return "paginaDeInicio"; // View name
//    }

//    @GetMapping("/paginaDeProducto")
//    public String paginaDeProducto(Model model)
//    {
//        return "paginaDeProducto"; // View name
//    }



    /**
     * Gestiona las solicitudes GET para obtener y mostrar la lista de entidades protegidas.
     * Añade las entidades obtenidas del repositorio al modelo para renderizarlas en la vista correspondiente.
     *
     * @param model Objeto {@link Model} que se utiliza para pasar datos desde el controlador a la vista.
     *              Contendrá la lista de entidades recuperadas desde el repositorio.
     * @return El nombre de la vista "entitiesList" donde se mostrará la lista de entidades.
     */
//    @GetMapping("/protected")
//    public String protectedList(Model model)
//    {
//        model.addAttribute("entidades", entidadPadreService.findAll());
//        return "entidadesPadre"; // View name
//    }


    /**
     * Deletes an EntidadHija entity by its ID using the EntidadHijaService.
     *
     * @param id The ID of the EntidadHija to delete.
     * @return A redirect to the "/protected" endpoint after deletion.
     */
//    @GetMapping("/entidades/deleteHija/{id}")
//    public String deleteEntidadHija(@PathVariable Long id) {
//        entidadHijaService.deleteById(id);
//        return "redirect:/entities";
//    }



    /**
     * Deletes an EntidadHija entity by its ID using the EntidadHijaService.
     *
     * @param id The ID of the EntidadHija to delete.
     * @return A redirect to the "/protected" endpoint after deletion.
     */
//    @PostMapping("/entidades/deletePadre/{id}")
//    public String deleteEntidadPadre(@PathVariable Long id) {
//        entidadPadreService.deleteById(id);
//        return "redirect:/entities";
//    }

}
