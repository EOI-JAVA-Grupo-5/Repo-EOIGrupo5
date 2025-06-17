package com.eoi.grupo5.services.foro;

import com.eoi.grupo5.entities.foro.Hilo;
import com.eoi.grupo5.entities.foro.MensajeHilo;
import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.foro.HiloRepository;
import com.eoi.grupo5.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HiloService {
    private final HiloRepository hiloRepository;
    private final MensajeService mensajeService;
    private final UsuarioService usuarioService;

    @Autowired
    public HiloService(HiloRepository hiloRepository, MensajeService mensajeService, UsuarioService usuarioService) {
        this.hiloRepository = hiloRepository;
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    public String showHilosEnForo(int page,
                                  String sort,
                                  String keyword,
                                  boolean misHilos,
                                  Model model,
                                  UserDetails userDetails) {

        Page<Hilo> hilosRecientes = obtenerHilosRecientesPaginado(page, 3);
        List<Hilo> hilosOrdenados = buscarYOrdenarHilos(keyword, sort, userDetails, misHilos);

        addModelAttributes(model, hilosOrdenados, hilosRecientes, page, sort, keyword, misHilos);
        return "foro/main";
    }

    public String showHilo(Long id, Model model, UserDetails userDetails) {

        Hilo hilo = findById(id);
        List<MensajeHilo> mensajes = mensajeService.findMessagesByHiloId(id);
        Usuario usuario = getAuthenticatedUsuario(userDetails);
        if (usuario == null) {
            return "redirect:/login";
        }

        addHiloAttributesToModel(model, hilo, mensajes, usuario, userDetails);
        return "foro/hilo";
    }

    public String createHilo(Hilo hilo, UserDetails userDetails) {
        Usuario usuario = getAuthenticatedUsuario(userDetails);
        if (usuario == null) {
            return "redirect:/login";
        }

        prepararNuevoHilo(hilo, usuario);
        guardarHilo(hilo);

        return "redirect:/foro/hilo/" + hilo.getId();
    }

    public String editHilo(Long id,
                           String titulo,
                           String descripcion,
                           UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {

        try {
            Hilo hilo = findById(id);
            if (canEditHilo(hilo, userDetails)) {
                updateHiloConDatos(hilo, titulo, descripcion);
                guardarHilo(hilo);
                redirectAttributes.addFlashAttribute("success", "Hilo editado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este hilo.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo editar el hilo.");
        }

        return "redirect:/foro/hilo/" + id;
    }

    public String deleteHilo(Long id,
                             UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {

        try {
            Hilo hilo = findById(id);
            if (canEditHilo(hilo, userDetails)) {
                eliminarHiloYMensajes(id);
                redirectAttributes.addFlashAttribute("success", "Hilo eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar este hilo.");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el hilo.");
        }

        return "redirect:/foro";
    }

    public Hilo guardarHilo(Hilo hilo) {
        return hiloRepository.save(hilo);
    }

    public Page<Hilo> obtenerHilosRecientesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return hiloRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    private List<Hilo> buscarYOrdenarHilos(String keyword, String sortType,
                                           UserDetails userDetails,
                                           boolean misHilos) {
        Sort sortCriteria = buildSortCriteria(sortType);

        if (userDetails == null || !misHilos) {
            if (keyword == null || keyword.trim().isEmpty()) {
                return hiloRepository.findAll(sortCriteria);
            } else {
                return hiloRepository.findByTituloContainingIgnoreCase(keyword, sortCriteria);
            }
        }

        String usuarioUsername = userDetails.getUsername();

        if (keyword == null || keyword.trim().isEmpty()) {
            return hiloRepository.findByAutor_Username(usuarioUsername, sortCriteria);
        } else {
            return hiloRepository.findByAutor_UsernameAndTituloContainingIgnoreCase(usuarioUsername, keyword, sortCriteria);
        }
    }

    // Metodos showHilosEnForo
    private void addModelAttributes(Model model,
                                    List<Hilo> hilosOrdenados,
                                    Page<Hilo> hilosRecientes,
                                    int page,
                                    String sort,
                                    String keyword,
                                    boolean misHilos) {

        model.addAttribute("hilos", hilosOrdenados);
        model.addAttribute("hilosRecientes", hilosRecientes);
        model.addAttribute("totalPages", hilosRecientes.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("nuevoHilo", new Hilo());
        model.addAttribute("selectedSort", sort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("misHilos", misHilos);
    }

    // Metodos para showHilo
    private Usuario getAuthenticatedUsuario(UserDetails userDetails) {
        return usuarioService.findByUsername(userDetails.getUsername())
                .orElse(null);
    }

    private void addHiloAttributesToModel(Model model, Hilo hilo, List<MensajeHilo> mensajes, Usuario usuario, UserDetails userDetails) {
        model.addAttribute("hilo", hilo);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("nuevoMensaje", new MensajeHilo());
        model.addAttribute("usuarioActual", usuario);
        model.addAttribute("isAdmin", isAdmin(userDetails));
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

    // Metodos para createHilo
    private void prepararNuevoHilo(Hilo hilo, Usuario usuario) {
        hilo.setFechaCreacion(LocalDateTime.now());
        hilo.setAutor(usuario);
    }

    // Metodos para editarHilo

    private boolean canEditHilo(Hilo hilo, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return hilo.getAutor().getUsername().equals(username) || isAdmin(userDetails);
    }

    private void updateHiloConDatos(Hilo hilo, String titulo, String descripcion) {
        hilo.setTitulo(titulo);
        hilo.setDescripcion(descripcion);
    }

    // Metodos para deleteHilo
    private void eliminarHiloYMensajes(Long id) {
        Hilo hilo = hiloRepository.findById(id).orElseThrow(() -> new RuntimeException("Hilo no encontrado"));

        for (MensajeHilo mensaje : hilo.getMensajes()) {
            mensajeService.deleteMessageById(mensaje.getId());
        }

        hiloRepository.delete(hilo);
    }

    // Metodos reutilizables y de otros metodos

    private Hilo findById(Long id) {
        return hiloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hilo no encontrado"));
    }

    private Sort buildSortCriteria (String sortType) {
        Sort sortCriteria;

        switch (sortType) {
            case "votos":
                sortCriteria = Sort.by(Sort.Direction.DESC, "votos");
                break;
            case "visitas":
                sortCriteria = Sort.by(Sort.Direction.DESC, "visitas");
                break;
            case "mensajes":
                sortCriteria = Sort.by(Sort.Direction.DESC, "mensajeCount");
                break;
            default:
                sortCriteria = Sort.by(Sort.Direction.DESC, "fechaCreacion");
                break;
        }
        return sortCriteria;
    }

}

