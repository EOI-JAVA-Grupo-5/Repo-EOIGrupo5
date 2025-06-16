package com.eoi.grupo5.services.foro;

import com.eoi.grupo5.entities.foro.EntidadHilo;
import com.eoi.grupo5.entities.foro.EntidadMensaje;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.foro.MensajeRepository;
import com.eoi.grupo5.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final UsuarioService usuarioService;
    private final HiloLookupService hiloLookupService;

    @Autowired
    public MensajeService(MensajeRepository mensajeRepository, UsuarioService usuarioService, HiloLookupService hiloLookupService) {
        this.mensajeRepository = mensajeRepository;
        this.usuarioService = usuarioService;
        this.hiloLookupService = hiloLookupService;
    }

    public String newMensaje(Long hiloId, EntidadMensaje mensaje, UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Usuario autor = usuarioService.findByUsername(userDetails.getUsername()).orElse(null);
        if (autor == null) {
            return "redirect:/login";
        }

        EntidadHilo hilo = hiloLookupService.findById(hiloId);
        preparareNewMensaje(mensaje, autor, hilo);

        saveMessage(mensaje);
        return "redirect:/foro/hilo/" + hiloId;
    }

    public String editMensaje(Long mensajeId,
                              String contenido,
                              UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            EntidadMensaje mensaje = findMessageById(mensajeId);

            if (!canEditMensaje(mensaje, userDetails)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este mensaje.");
                return redirectToHilo(mensaje);
            }

            actualizarContenidoMensaje(mensaje, contenido);
            saveMessage(mensaje);
            redirectAttributes.addFlashAttribute("success", "Mensaje editado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al editar el mensaje.");
        }

        // Always redirect to the hilo page of the mensaje
        EntidadMensaje mensaje = findMessageById(mensajeId);
        return redirectToHilo(mensaje);
    }

    public String deleteMensaje(Long mensajeId, UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            EntidadMensaje mensaje = findMessageById(mensajeId);

            if (!canEditMensaje(mensaje, userDetails)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar este mensaje.");
                return redirectToHilo(mensaje);
            }

            Long hiloId = mensaje.getHilo().getId();
            deleteMessageById(mensajeId);
            redirectAttributes.addFlashAttribute("success", "Mensaje eliminado correctamente.");
            return "redirect:/foro/hilo/" + hiloId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el mensaje.");
            return "redirect:/foro";
        }
    }


    // Metodos para newMensaje
    private void preparareNewMensaje(EntidadMensaje mensaje, Usuario autor, EntidadHilo hilo) {
        mensaje.setId(null);
        mensaje.setAutor(autor);
        mensaje.setHilo(hilo);
        mensaje.setFechaCreacion(LocalDateTime.now());
    }

    // Metodos para editMensaje
    private boolean canEditMensaje(EntidadMensaje mensaje, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return mensaje.getAutor().getUsername().equals(username) || isAdmin(userDetails);
    }

    private void actualizarContenidoMensaje(EntidadMensaje mensaje, String contenido) {
        mensaje.setContenido(contenido);
    }

    private String redirectToHilo(EntidadMensaje mensaje) {
        return "redirect:/foro/hilo/" + mensaje.getHilo().getId();
    }

    // Metodos para deleteMensaje

    // Metodos para HiloService y reutilizables
    public List<EntidadMensaje> findMessagesByHiloId(Long hiloId) {
        return mensajeRepository.findByHilo_IdOrderByFechaCreacionAsc(hiloId);
    }

    public void saveMessage(EntidadMensaje mensaje) {
        mensajeRepository.save(mensaje);
    }

    public EntidadMensaje findMessageById(Long id) {

        EntidadMensaje mensaje = mensajeRepository.findById(id).get();

        if (mensaje == null) {
            throw new EntityNotFoundException("Mensaje no encontrado");
        }

        return mensaje;
    }

    @Transactional
    public void deleteMessageById(Long id) {
        EntidadMensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        EntidadHilo hilo = mensaje.getHilo();
        if (hilo != null) {
            hilo.getMensajes().remove(mensaje);
        }

        hiloLookupService.saveHilo(hilo);
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

}
