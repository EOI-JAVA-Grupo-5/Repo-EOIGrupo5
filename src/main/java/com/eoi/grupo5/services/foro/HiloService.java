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

    private static final String FLASH_ERROR = "error";

    @Autowired
    public HiloService(HiloRepository hiloRepository, MensajeService mensajeService, UsuarioService usuarioService) {
        this.hiloRepository = hiloRepository;
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    // Controller Methods

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

        addViewCount(hilo);

        addHiloAttributesToModel(model, hilo, mensajes, usuario, userDetails);
        return "foro/hilo";
    }

    public String createHilo(Hilo hilo, UserDetails userDetails) {
        Usuario usuario = getAuthenticatedUsuario(userDetails);
        if (usuario == null) {
            return "redirect:/login";
        }

        prepararNuevoHilo(hilo, usuario);
        saveHilo(hilo);

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
                saveHilo(hilo);
                redirectAttributes.addFlashAttribute("success", "Hilo editado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute(FLASH_ERROR, "No tienes permiso para editar este hilo.");
            }
        } catch (Exception _) {
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "No se pudo editar el hilo.");
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
                redirectAttributes.addFlashAttribute(FLASH_ERROR, "No tienes permiso para eliminar este hilo.");
            }

        } catch (Exception _) {
            redirectAttributes.addFlashAttribute(FLASH_ERROR, "Error al eliminar el hilo.");
        }

        return "redirect:/foro";
    }

    public void saveHilo(Hilo hilo) {
        hiloRepository.save(hilo);
    }

    public String likeHilo(Long id, UserDetails userDetails) {

        if (userDetails == null) {
            throw  new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        Hilo hilo = findById(id);
        hilo.setVotos(hilo.getVotos() + 1);
        hiloRepository.save(hilo);
        return "redirect:/foro/hilo/" + hilo.getId();
    }

    // Methods showHilosEnForo
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

    public Page<Hilo> obtenerHilosRecientesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return hiloRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    private List<Hilo> buscarYOrdenarHilos(String keyword,
                                           String sortType,
                                           UserDetails userDetails,
                                           boolean misHilos) {

        Sort sort = buildSortCriteria(sortType);
        boolean isKeywordEmpty = (keyword == null || keyword.trim().isEmpty());
        String username = (userDetails != null) ? userDetails.getUsername() : null;

        if (!misHilos || username == null) {
            return buscarHilosPublicos(keyword, isKeywordEmpty, sort);
        }

        return buscarHilosDelUsuario(username, keyword, isKeywordEmpty, sort);
    }

    private List<Hilo> buscarHilosPublicos(String keyword, boolean isKeywordEmpty, Sort sort) {
        return isKeywordEmpty
                ? hiloRepository.findAll(sort)
                : hiloRepository.findByTituloContainingIgnoreCase(keyword, sort);
    }

    private List<Hilo> buscarHilosDelUsuario(String username, String keyword, boolean isKeywordEmpty, Sort sort) {
        return isKeywordEmpty
                ? hiloRepository.findByAutor_Username(username, sort)
                : hiloRepository.findByAutor_UsernameAndTituloContainingIgnoreCase(username, keyword, sort);
    }

    // Methods para showHilo
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

    private void addViewCount (Hilo hilo) {

        // Very basic view count would need to implement a better version if app gets bigger
        hilo.setVisitas(hilo.getVisitas() + 1);
        saveHilo(hilo);
    }

    // Methods to createHilo
    private void prepararNuevoHilo(Hilo hilo, Usuario usuario) {
        hilo.setFechaCreacion(LocalDateTime.now());
        hilo.setAutor(usuario);
    }

    // Methods to editarHilo

    private boolean canEditHilo(Hilo hilo, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return hilo.getAutor().getUsername().equals(username) || isAdmin(userDetails);
    }

    private void updateHiloConDatos(Hilo hilo, String titulo, String descripcion) {
        hilo.setTitulo(titulo);
        hilo.setDescripcion(descripcion);
    }

    // Methods to deleteHilo
    private void eliminarHiloYMensajes(Long id) {
        Hilo hilo = hiloRepository.findById(id).orElseThrow(() -> new RuntimeException("Hilo no encontrado"));

        for (MensajeHilo mensaje : hilo.getMensajes()) {
            mensajeService.deleteMessageById(mensaje.getId());
        }

        hiloRepository.delete(hilo);
    }

    // Methods reutilizables y de otros metodos

    private Hilo findById(Long id) {
        return hiloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hilo no encontrado"));
    }

    private Sort buildSortCriteria (String sortType) {
        return switch (sortType) {
            case "votos" -> Sort.by(Sort.Direction.DESC, "votos");
            case "visitas" -> Sort.by(Sort.Direction.DESC, "visitas");
            case "mensajes" -> Sort.by(Sort.Direction.DESC, "mensajeCount");
            default -> Sort.by(Sort.Direction.DESC, "fechaCreacion");
        };
    }

}

