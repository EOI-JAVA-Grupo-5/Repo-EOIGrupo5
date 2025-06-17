package com.eoi.grupo5.services.foro;

import com.eoi.grupo5.entities.foro.Hilo;
import com.eoi.grupo5.entities.foro.MensajeHilo;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.foro.MensajeRepository;
import com.eoi.grupo5.services.UsuarioService;
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

    private static final String REDIRECT_HILO = "redirect:/foro/hilo/";
    private static final String FLASH_ERROR = "error";

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
        return REDIRECT_HILO + hiloId;
    }

    public String editMensaje(Long mensajeId,
                              String contenido,
                              UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            MensajeHilo mensaje = findMessageById(mensajeId);

            if (cantEditMensaje(mensaje, userDetails)) {
                redirectAttributes.addFlashAttribute(FLASH_ERROR, "No tienes permiso para editar este mensaje.");
                return redirectToHilo(mensaje);
            }

            actualizarContenidoMensaje(mensaje, contenido);
            saveMessage(mensaje);
            redirectAttributes.addFlashAttribute("success", "Mensaje editado correctamente.");

        } catch (Exception _) {
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "Error al editar el mensaje.");
        }

        // Always redirect to the hilo page of the mensaje
        MensajeHilo mensaje = findMessageById(mensajeId);
        return redirectToHilo(mensaje);
    }

    public String deleteMensaje(Long mensajeId, UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            MensajeHilo mensaje = findMessageById(mensajeId);

            if (cantEditMensaje(mensaje, userDetails)) {
                redirectAttributes.addFlashAttribute(FLASH_ERROR, "No tienes permiso para eliminar este mensaje.");
                return redirectToHilo(mensaje);
            }

            Long hiloId = mensaje.getHilo().getId();
            deleteMessageById(mensajeId);
            redirectAttributes.addFlashAttribute("success", "Mensaje eliminado correctamente.");
            return REDIRECT_HILO + hiloId;

        } catch (Exception _) {
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "Error al eliminar el mensaje.");
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
    private boolean cantEditMensaje(MensajeHilo mensaje, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return !mensaje.getAutor().getUsername().equals(username) && !isAdmin(userDetails);
    }

    private void actualizarContenidoMensaje(MensajeHilo mensaje, String contenido) {
        mensaje.setContenido(contenido);
    }

    private String redirectToHilo(MensajeHilo mensaje) {
        return REDIRECT_HILO + mensaje.getHilo().getId();
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
        return mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));
    }

    @Transactional
    public void deleteMessageById(Long id) {
        MensajeHilo mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        Hilo hilo = mensaje.getHilo();
        if (hilo != null) {
            hilo.getMensajes().remove(mensaje);
            hilo.setMensajeCount(hilo.getMensajeCount() - 1);
            hiloLookupService.saveHilo(hilo);
        }

        mensajeRepository.delete(mensaje);
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

}
