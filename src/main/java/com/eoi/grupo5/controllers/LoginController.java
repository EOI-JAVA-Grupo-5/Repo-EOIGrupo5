package com.eoi.grupo5.controllers;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import com.eoi.grupo5.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

//    private UsuarioRepository usuarioRepository;
//    private UsuarioService usuarioService;

//    public LoginController(UsuarioRepository usuarioRepository, UsuarioService usuarioService){
//        this.usuarioRepository = usuarioRepository;
//        this.usuarioService = usuarioService;
//    }

    @ModelAttribute(name="usuario")
    public Usuario usuario(){
        log.info("-CREANDO USUARIO VACIO-");
        return new Usuario();
    }

    /**
     * Mapeo GET para la página de registro
     * @param model
     * @return pagina de login
     */

    @GetMapping
    public String mostrarPagina(Model model){
        return "login";
    }

//
//    /**
//     * Al hacer POST, se guarda el usuario en la base de datos
//     * @param usuario - Usuario para iniciar sesion
//     * @param errors
//     * @param model
//     * @return vuelve a "registro" si hay errores, redirige a "login" si todo ha ido bien
//     */
//    @PostMapping
//    public String loginUsuario(@Valid @ModelAttribute(name = "usuario") Usuario usuario, Errors errors, Model model){
//
//        log.info("Intentando LOGIN");
//        if(errors.hasErrors()){
//            log.info("-ERROR EN LOGIN-");
//            return "login";
//        }
//
//        //Buscamos si el correo del usuario proporcionado existe en la BBDD
//        Optional<Usuario> encontrado = usuarioRepository.findByUsername(usuario.getUsername());
//
//        //Si existe
//        if (encontrado.isPresent()){
//            //Pasamos de Optional a Usuario
//            Usuario usuarioBD = encontrado.get();
//
//            //Comprobamos que la contraseña introducida sea igual a la contraseña guardada en BBDD
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//            if(encoder.matches(usuario.getPassword(), usuarioBD.getPassword())){
//                log.info("-LOGIN CORRECTO-");
//                return "redirect:/usuario";
//            }else{
//                log.info("-CONTRASEÑA INCORRECTA PARA USUARIO " + usuarioBD.getUsername());
//                return "redirect:/inicioSesion";
//            }
//
//        }else{
//            log.info("-USUARIO NO ENCONTRADO-");
//            return "redirect:/inicioSesion";
//        }
//
//    }
}
