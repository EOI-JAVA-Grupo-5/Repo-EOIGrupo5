package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.entities.EntidadMensaje;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.services.HiloService;
import com.eoi.grupo5.services.MensajeService;
import com.eoi.grupo5.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/foro")
public class ForoController {

    private final HiloService hiloService;
    private final MensajeService mensajeService;

    // Temporary While Jose Angel makes the main one.
    private final UsuarioService usuarioService;

    @Autowired
    public ForoController(HiloService hiloService, MensajeService mensajeService, UsuarioService usuarioService) {
        this.hiloService = hiloService;
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String mostrarForo(Model model) {
        List<EntidadHilo> hilos = hiloService.obtenerHilos();
        // TODO get the amount of messages on each hilo before sending
        model.addAttribute("hilos", hilos);
        model.addAttribute("nuevoHilo", new EntidadHilo());
        return "foro/main";
    }

    @PostMapping("/crear")
    public String crearHilo(@ModelAttribute("nuevoHilo") EntidadHilo hilo) {
        hilo.setFechaCreacion(LocalDateTime.now());
        Usuario autor = usuarioService.obtenerUsuarioPorId(1L); // Temporal, until login works
        hilo.setAutor(autor);
        hiloService.guardarHilo(hilo);
        return "redirect:/foro/hilo/" + hilo.getId();
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

    @PostMapping("/hilo/{id}/mensaje")
    public String agregarMensaje(@PathVariable Long id, @ModelAttribute("nuevoMensaje") EntidadMensaje mensaje) {
        EntidadHilo hilo = hiloService.obtenerHiloPorId(id);
        Usuario autor = usuarioService.obtenerUsuarioPorId(1L); // Temporal, until login works
        mensaje.setHilo(hilo);
        mensaje.setAutor(autor);
        mensaje.setFechaPublicacion(LocalDateTime.now());
        mensajeService.guardarMensaje(mensaje);
        return "redirect:/foro/hilo/" + id;
    }
}
