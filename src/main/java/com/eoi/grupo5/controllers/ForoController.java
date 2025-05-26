package com.eoi.grupo5.controllers;

import com.eoi.grupo5.services.HiloService;
import com.eoi.grupo5.services.MensajeService;
import com.eoi.grupo5.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foro")
public class ForoController {

    private HiloService hiloService;
    private MensajeService mensajeService;

    // Temporary While Jose Angel makes the main one.
    private UsuarioService usuarioService;

    @Autowired
    public ForoController(HiloService hiloService, MensajeService mensajeService, UsuarioService usuarioService) {
        this.hiloService = hiloService;
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarHilos(Model model) {
        model.addAttribute("hilos", hiloService.obtenerHilos());
        return "foro/main";
    }
}
