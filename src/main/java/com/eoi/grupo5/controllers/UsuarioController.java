package com.eoi.grupo5.controllers;

import com.eoi.grupo5.dtos.DatosGraficaDTO;
import com.eoi.grupo5.dtos.PasswordModDTO;
import com.eoi.grupo5.entities.Producto;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import com.eoi.grupo5.services.TablaUsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final TablaUsuarioService tablaUsuarioService;

    public UsuarioController(UsuarioRepository usuarioRepository, TablaUsuarioService tablaUsuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.tablaUsuarioService = tablaUsuarioService;
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

        List<DatosGraficaDTO> datos = tablaUsuarioService.datosUltimaSemana(usuario);

        List<Date> fechas = tablaUsuarioService.datosDiasUltimaSemana(datos);
        List<Double> dineroGastado = tablaUsuarioService.datosDineroUltimaSemana(datos);

        List<String> strings = new ArrayList<>();
        List<Number> gastos = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate;
        for(int i = 0; i<fechas.size(); i++)
        {
            localDate = fechas.get(i).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            strings.add(localDate.format(formatter));
            gastos.add(dineroGastado.get(i));
        }

        model.addAttribute("datosFechas", strings);
        model.addAttribute("datosDinero", gastos);

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
    public String modificarDatosUsuario(@Valid @ModelAttribute(name = "datos_usuario") Usuario datos, Principal principal,RedirectAttributes redirectAttributes, Errors errors, Model model){
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
        redirectAttributes.addFlashAttribute("modificado", "Los datos del usuario fueron modificados");

        return "redirect:/usuario/modificar";
    }


    /**
     * Muestra la página de modificación de contraseña
     * @param userDetails - Detalles del usuario
     * @param model - Modelo
     * @return Página de modificación de contraseña
     */
    @GetMapping("/usuario/modificar/password")
    public String modificarPasswordUsuario(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        return "modificarPassword";
    }


    /**
     * Método que modifica la contraseña del usuario
     * @param passwords - DTO con tres contraseñas: la actual, la nueva y la nueva repetida
     * @param principal - Usuario principal
     * @param redirectAttributes - Atributos usados para los mensajes de error
     * @param errors - Errores
     * @param model - Modelo
     * @return Redirecciones a la página con un mensaje de error o mensaje de éxito (cambia la contraseña)
     */
    @PostMapping("/usuario/modificar/password")
    public String modificarPasswordUsuario(@Valid @ModelAttribute(name = "passwords") PasswordModDTO passwords, Principal principal, RedirectAttributes redirectAttributes , Errors errors, Model model){
        if(errors.hasErrors()){
            return "register";
        }

        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(passwords.getClaveActual(), usuario.getPassword())){
            log.info("CONTRASEÑA ACTUAL INCORRECTA");
            redirectAttributes.addFlashAttribute("fallo", "Contraseña actual incorrecta");

            return "redirect:/usuario/modificar/password?error";
        }
        else if(!passwords.getClaveNueva().equals(passwords.getClaveNuevaRepe())){
            log.info("CONTRASEÑAS NUEVAS INCORRECTA");
            redirectAttributes.addFlashAttribute("fallo","Los campos de nueva contraseña no coinciden");

            return "redirect:/usuario/modificar/password?error";
        }
        else {
            usuario.setPassword(encoder.encode(passwords.getClaveNuevaRepe()));
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("exito","Contraseña actualizada");

            log.info("CONTRASEÑA ACTUALIZADA");
            return "redirect:/usuario/modificar/password?exito";
        }
    }


    /**
     * Sube la foto de un usuario. Si el usuario ya tenía una foto guardada, reemplaza la foto.
     * @param foto - Archivo de imagen de la foto
     * @param principal - Principal
     * @param redirectAttributes - Atributos de redirección
     * @return Cambia la foto o muestra un mensaje de error
     * @throws IOException
     */
    @PostMapping("/usuario/modificar/subirFoto")
    public String subirFoto(@RequestParam("foto") MultipartFile foto, Principal principal, RedirectAttributes redirectAttributes) throws IOException {

        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        if(foto.isEmpty()){
            redirectAttributes.addFlashAttribute("fotoInvalida", "No se subió una imagen válida");

            return "redirect:/usuario/modificar";
        }else{
            if(usuario.getImagenURL() != null){
                Files.delete(Paths.get("uploads/perfiles/" + usuario.getImagenURL()));
            }
            String nombreArchivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
            Path rutaDestino = Paths.get("uploads/perfiles/" + nombreArchivo);
            Files.createDirectories(rutaDestino.getParent()); // crea la carpeta si no existe
            Files.copy(foto.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            log.info("ARCHIVO: " + nombreArchivo);

            usuario.setImagenURL(nombreArchivo);
            usuarioRepository.save(usuario);

            log.info("URL: " + usuario.getImagenURL());

            redirectAttributes.addFlashAttribute("modificado", "Se actualizó la foto de perfil");

            return "redirect:/usuario/modificar";
        }

    }
}
