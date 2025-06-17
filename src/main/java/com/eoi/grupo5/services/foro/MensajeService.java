package com.eoi.grupo5.services.foro;

import com.eoi.grupo5.entities.foro.Hilo;
import com.eoi.grupo5.entities.foro.MensajeHilo;
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

    public String newMensaje(Long hiloId, MensajeHilo mensaje, UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Usuario autor = usuarioService.findByUsername(userDetails.getUsername()).orElse(null);
        if (autor == null) {
            return "redirect:/login";
        }

        Hilo hilo = hiloLookupService.findById(hiloId);
        preparareNewMensaje(mensaje, autor, hilo);

        saveMessage(mensaje);
        return "redirect:/foro/hilo/" + hiloId;
    }

    public String editMensaje(Long mensajeId,
                              String contenido,
                              UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            MensajeHilo mensaje = findMessageById(mensajeId);

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
        MensajeHilo mensaje = findMessageById(mensajeId);
        return redirectToHilo(mensaje);
    }

    public String deleteMensaje(Long mensajeId, UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            MensajeHilo mensaje = findMessageById(mensajeId);

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
    private void preparareNewMensaje(MensajeHilo mensaje, Usuario autor, Hilo hilo) {
        mensaje.setId(null);
        mensaje.setAutor(autor);
        mensaje.setHilo(hilo);
        mensaje.setFechaCreacion(LocalDateTime.now());
        hilo.setMensajeCount(hilo.getMensajeCount() + 1);
        hiloLookupService.saveHilo(hilo);
    }

    // Metodos para editMensaje
    private boolean canEditMensaje(MensajeHilo mensaje, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return mensaje.getAutor().getUsername().equals(username) || isAdmin(userDetails);
    }

    private void actualizarContenidoMensaje(MensajeHilo mensaje, String contenido) {
        mensaje.setContenido(contenido);
    }

    private String redirectToHilo(MensajeHilo mensaje) {
        return "redirect:/foro/hilo/" + mensaje.getHilo().getId();
    }

    // Metodos para deleteMensaje

    // Metodos para HiloService y reutilizables
    public List<MensajeHilo> findMessagesByHiloId(Long hiloId) {
        return mensajeRepository.findByHilo_IdOrderByFechaCreacionAsc(hiloId);
    }

    public void saveMessage(MensajeHilo mensaje) {
        mensajeRepository.save(mensaje);
    }

    public MensajeHilo findMessageById(Long id) {

        MensajeHilo mensaje = mensajeRepository.findById(id).get();

        if (mensaje == null) {
            throw new EntityNotFoundException("Mensaje no encontrado");
        }

        return mensaje;
    }

    @Transactional
    public void deleteMessageById(Long id) {
        MensajeHilo mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        Hilo hilo = mensaje.getHilo();
        if (hilo != null) {
            hilo.getMensajes().remove(mensaje);
            hilo.setMensajeCount(hilo.getMensajeCount() - 1);
        }

        hiloLookupService.saveHilo(hilo);
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

}
