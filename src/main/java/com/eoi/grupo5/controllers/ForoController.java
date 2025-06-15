package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.entities.EntidadMensaje;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.services.HiloService;
import com.eoi.grupo5.services.MensajeService;
import com.eoi.grupo5.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/foro")
public class ForoController {

    private final HiloService hiloService;
    private final MensajeService mensajeService;
    private final UsuarioService usuarioService;

    @Autowired
    public ForoController(HiloService hiloService, MensajeService mensajeService, UsuarioService usuarioService) {
        this.hiloService = hiloService;
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String mostrarForo(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "votos") String sort,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "false") boolean misHilos,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {

        // Sidebar items
        int size = 3;
        Page<EntidadHilo> hilosRecientes = hiloService.obtenerHilosRecientesPaginado(page, size);

        // Main Content cards
        List<EntidadHilo> hilosOrdenados = hiloService.buscarYOrdenarHilos(keyword, sort, userDetails, misHilos);

        // Models
        model.addAttribute("hilos", hilosOrdenados);
        model.addAttribute("hilosRecientes", hilosRecientes);
        model.addAttribute("totalPages", hilosRecientes.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("nuevoHilo", new EntidadHilo());
        model.addAttribute("selectedSort", sort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("misHilos", misHilos);

        return "foro/main";
    }

    @PostMapping("/crear")
    public String crearHilo(@ModelAttribute("nuevoHilo") EntidadHilo hilo,
                            @AuthenticationPrincipal UserDetails userDetails) {
        hilo.setFechaCreacion(LocalDateTime.now());

        if (userDetails != null) {
            String username = userDetails.getUsername();
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);

            if (usuarioOpt.isPresent()) {
                hilo.setAutor(usuarioOpt.get());
                hiloService.guardarHilo(hilo);
                return "redirect:/foro/hilo/" + hilo.getId();
            }
        }
        // Fallback (shouldn't happen if Security is configured properly)
        return "redirect:/login";
    }

    @GetMapping("/hilo/{id}")
    public String obtenerHilo(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        EntidadHilo hilo = hiloService.obtenerHiloPorId(id);
        List<EntidadMensaje> mensajes = mensajeService.findMessagesByHiloId(id);

        Optional<Usuario> usuarioActual = usuarioService.findByUsername(userDetails.getUsername());
        Usuario usuario = null;
        if (usuarioActual.isPresent()) {
            usuario = usuarioActual.get();
        }

        model.addAttribute("hilo", hilo);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("nuevoMensaje", new EntidadMensaje());
        model.addAttribute("usuarioActual", usuario);
        return "foro/hilo";
    }

    @PostMapping("/hilo/{id}")
    public String agregarMensaje(@PathVariable Long id,
                                 @ModelAttribute("nuevoMensaje") EntidadMensaje mensaje,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        EntidadHilo hilo = hiloService.obtenerHiloPorId(id);

        if (userDetails != null) {

            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(userDetails.getUsername());

            if (usuarioOpt.isPresent()) {
                Usuario autor = usuarioOpt.get();

                mensaje.setId(null);
                mensaje.setAutor(autor);
                mensaje.setHilo(hilo);
                mensaje.setFechaCreacion(LocalDateTime.now());

                mensajeService.saveMessage(mensaje);
                return "redirect:/foro/hilo/" + id;
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/editar/{id}")
    public String editarHilo(@PathVariable Long id,
                             @RequestParam String titulo,
                             @RequestParam String descripcion,
                             RedirectAttributes redirectAttributes) {

        try {
            EntidadHilo hilo = hiloService.obtenerHiloPorId(id);
            hilo.setTitulo(titulo);
            hilo.setDescripcion(descripcion);
            hiloService.guardarHilo(hilo);

            redirectAttributes.addFlashAttribute("success", "Hilo editado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo editar el hilo.");
        }


        return "redirect:/foro/hilo/" + id;
    }

    @PostMapping("/mensaje/editar/{id}")
    public String editarMensaje(@PathVariable Long id,
                                @RequestParam String contenido,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            EntidadMensaje mensaje = mensajeService.findMessageById(id);
            if (mensaje.getAutor().getUsername().equals(userDetails.getUsername())) {
                mensaje.setContenido(contenido);
                mensajeService.saveMessage(mensaje);
                redirectAttributes.addFlashAttribute("success", "Mensaje editado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este mensaje.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al editar el mensaje.");
        }
        return "redirect:/foro/hilo/" + mensajeService.findMessageById(id).getHilo().getId();
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarHilo(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {


        try {
            hiloService.eliminarHiloYMensajes(id);
            redirectAttributes.addFlashAttribute("success", "Hilo eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el hilo.");
        }
        return "redirect:/foro";
    }

    @PostMapping("/mensaje/eliminar/{id}")
    public String eliminarMensaje(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        System.out.println("Attempting to delete mensaje id: " + id);
        try {
            EntidadMensaje mensaje = mensajeService.findMessageById(id);
            System.out.println("Mensaje found: " + mensaje.getContenido());
            if (mensaje.getAutor().getUsername().equals(userDetails.getUsername())) {
                Long hiloId = mensaje.getHilo().getId();
                mensajeService.deleteMessageById(id);
                System.out.println("Mensaje deleted");
                redirectAttributes.addFlashAttribute("success", "Mensaje eliminado correctamente.");
                return "redirect:/foro/hilo/" + hiloId;
            } else {
                System.out.println("User not authorized to delete this message");
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar este mensaje.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el mensaje.");
        }
        return "redirect:/foro"; // fallback
    }
}
