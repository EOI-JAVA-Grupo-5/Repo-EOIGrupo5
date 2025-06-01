package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import com.eoi.grupo5.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/registro")
public class RegisterController {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    public RegisterController(UsuarioRepository usuarioRepository, UsuarioService usuarioService){
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @ModelAttribute(name="usuario")
    public Usuario usuario(){
        log.info("-CREANDO NUEVO USUARIO-");
        return new Usuario();
    }

    @GetMapping
    public String mostrarPagina(Model model){
        return "register";
    }

    @PostMapping
    public String procesarNuevoUsuario(@Valid @ModelAttribute(name = "usuario") Usuario registro, Errors errors, Model model){
        if(errors.hasErrors()){
            return "register";
        }

        Usuario guardado = usuarioRepository.save(registro);
        log.info("-Procesando nuevo usuario: " + guardado);

        return "redirect:/login";
    }
}
