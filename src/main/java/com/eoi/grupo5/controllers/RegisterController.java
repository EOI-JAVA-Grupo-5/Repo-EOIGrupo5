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

    @ModelAttribute(name="reg_user")
    public Usuario usuario(){
        log.info("-CREANDO NUEVO USUARIO-");
        return new Usuario();
    }

    /**
     * Mapeo GET para la página de registro
     * @param model - Modelo
     * @return Pagina de registro
     */

    @GetMapping
    public String mostrarPagina(Model model){
        return "register";
    }


    /**
     * Al hacer POST, se guarda el usuario en la base de datos o vuelve a la pagina de registro si hay errores
     * @param registro - Usuario a registrar (username, correo y contraseña)
     * @param errors - Errores
     * @param model - Modelo
     * @return redirige a "registro" si hay errores, redirige a "login" si todo ha ido bien
     */
    @PostMapping
    public String procesarNuevoUsuario(@Valid @ModelAttribute(name = "reg_user") Usuario registro, Errors errors, Model model){
        if(errors.hasErrors()){
            return "register";
        }

        //Comprueba que el correo y el usuario existan en la BBDD. Si ya existen, devuelve un error. Si no, registra al usuario.
        if (usuarioRepository.findByCorreoEquals(registro.getCorreo()).isPresent()){
            log.info("-EL CORREO YA FUE REGISTRADO-");

            return "redirect:registro?error=El correo ya fue registrado";

        } else if (usuarioService.findByUsername(registro.getUsername()).isPresent()) {
            log.info("-EL NICK YA ESTÁ EN USO-");

            return "redirect:registro?error=El nombre de usuario ya esta en uso";
        } else{
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            Usuario guardado = registro;

            guardado.setNombre("-");
            guardado.setApellidos("-");
            guardado.setTipo(Usuario.Tipo.USER);

            guardado.setPassword(passwordEncoder.encode(registro.getPassword()));

            usuarioRepository.save(guardado);

            log.info("-Procesando nuevo usuario: " + guardado);

            return "redirect:/login";
        }

    }
}
