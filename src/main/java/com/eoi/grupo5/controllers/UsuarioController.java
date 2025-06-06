package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * GET que devuelve la página de perfil del usuario y muestra sus datos
     * @param userDetails - Detalles del usuario con sesión iniciada
     * @param model - Modelo
     * @return Muestra el perfil del usuario
     */
    @GetMapping("/usuario")
    public String perfilUsuario(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        return "perfilUsuario";
    }
}
