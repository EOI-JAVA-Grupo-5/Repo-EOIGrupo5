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
    public String obtenerHilo(@PathVariable Long id, Model model) {
        EntidadHilo hilo = hiloService.obtenerHiloPorId(id);
        List<EntidadMensaje> mensajes = mensajeService.findMessagesByHiloId(id);
        model.addAttribute("hilo", hilo);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("nuevoMensaje", new EntidadMensaje());
        return "foro/hilo";
    }

//    @PostMapping("/hilo/{id}/mensaje")
//    public String agregarMensaje(@PathVariable Long id, @ModelAttribute("nuevoMensaje") EntidadMensaje mensaje) {
//        EntidadHilo hilo = hiloService.obtenerHiloPorId(id);
//        Usuario autor = usuarioService.obtenerUsuarioPorId(1L); // Temporal, until login works
//        mensaje.setHilo(hilo);
//        mensaje.setAutor(autor);
//        mensaje.setFechaPublicacion(LocalDateTime.now());
//        mensajeService.guardarMensaje(mensaje);
//        return "redirect:/foro/hilo/" + id;
//    }
}
