package com.eoi.grupo5.controllers;

import com.eoi.grupo5.dtos.PasswordModDTO;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
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

    /**
     * GET que devuelve la página para modificar los datos del usuario
     * @param userDetails - Detalles del usuario con sesión iniciada
     * @param model - Modelo
     * @return Muestra una página donde se pueden modificar los datos del usuario
     */
    @GetMapping("/usuario/modificar")
    public String modificarUsuario(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        return "modificarUsuario";
    }

    /**
     *
     * @param datos - Datos del usuario introducidos para modificar
     * @param principal - Usuario con sesión iniciada
     * @param errors - Errores
     * @param model - Modelo
     * @return
     */
    @PostMapping("/usuario/modificar")
    public String modificarDatosUsuario(@Valid @ModelAttribute(name = "datos_usuario") Usuario datos, Principal principal, Errors errors, Model model){
        if(errors.hasErrors()){
            return "register";
        }

        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        //Si algún campo no ha sido rellenado en el formulario, no se produce cambio (queda como estaba)
        if(!datos.getNombre().isEmpty()){
            usuario.setNombre(datos.getNombre());
        }
        if(!datos.getApellidos().isEmpty()){
            usuario.setApellidos(datos.getApellidos());
        }
        if(!datos.getTelefono().isEmpty()){
            usuario.setTelefono(datos.getTelefono());
        }

        usuarioRepository.save(usuario);

        return "redirect:/usuario";
    }



    @GetMapping("/usuario/modificar/password")
    public String modificarPasswordUsuario(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        return "modificarPassword";
    }


    @PostMapping("/usuario/modificar/password")
    public String modificarPasswordUsuario(@Valid @ModelAttribute(name = "passwords") PasswordModDTO passwords, Principal principal, Errors errors, Model model){
        if(errors.hasErrors()){
            return "register";
        }

        log.info(passwords.getClaveActual() + " " + passwords.getClaveNueva() + " " + passwords.getClaveNuevaRepe());

        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(passwords.getClaveActual(), usuario.getPassword())){
            log.info("CONTRASEÑA ACTUAL INCORRECTA");
            model.addAttribute("fallo", "Contraseña actual incorrecta");
            return "redirect:/usuario/modificar/password";
        }
        else if(!passwords.getClaveNueva().equals(passwords.getClaveNuevaRepe())){
            log.info("CONTRASEÑAS NUEVAS INCORRECTA");
            model.addAttribute("fallo", "Los campos de nueva contraseña no coinciden");
            return "redirect:/usuario/modificar/password";
        }
        else {
            usuario.setPassword(encoder.encode(passwords.getClaveNuevaRepe()));
            usuarioRepository.save(usuario);
            model.addAttribute("exito", "Contraseña actualizada");
            log.info("CONTRASEÑA ACTUALIZADA");
        }

        return "redirect:/usuario/modificar/password";
    }
}
