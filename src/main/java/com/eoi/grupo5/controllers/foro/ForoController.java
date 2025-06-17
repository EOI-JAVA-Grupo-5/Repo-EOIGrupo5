package com.eoi.grupo5.controllers.foro;

import com.eoi.grupo5.entities.foro.Hilo;
import com.eoi.grupo5.entities.foro.MensajeHilo;
import com.eoi.grupo5.services.foro.HiloService;
import com.eoi.grupo5.services.foro.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/foro")
public class ForoController {

    private final HiloService hiloService;
    private final MensajeService mensajeService;

    @Autowired
    public ForoController(HiloService hiloService, MensajeService mensajeService) {
        this.hiloService = hiloService;
        this.mensajeService = mensajeService;
    }

    @GetMapping
    public String showHilosEnForo(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "votos") String sort,
                                  @RequestParam(defaultValue = "") String keyword,
                                  @RequestParam(defaultValue = "false") boolean misHilos,
                                  Model model,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        return hiloService.showHilosEnForo(page, sort, keyword, misHilos, model, userDetails);
    }

    @PostMapping
    public String createHilo(@ModelAttribute("nuevoHilo") Hilo hilo,
                             @AuthenticationPrincipal UserDetails userDetails) {

        return hiloService.createHilo(hilo, userDetails);
    }

    @GetMapping("/hilo/{id}")
    public String showHilo(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {

        return hiloService.showHilo(id, model, userDetails);
    }

    @PostMapping("/edit-hilo/{id}")
    public String editHilo(@PathVariable Long id,
                           @RequestParam String titulo,
                           @RequestParam String descripcion,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {

        return hiloService.editHilo(id, titulo, descripcion, userDetails, redirectAttributes);
    }

    @PostMapping("/delete-hilo/{id}")
    public String deleteHilo(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {

        return hiloService.deleteHilo(id, userDetails, redirectAttributes);
    }

    @PostMapping("/hilo/{id}/nuevo-mensaje")
    public String newMensaje(@PathVariable Long id,
                             @ModelAttribute("nuevoMensaje") MensajeHilo mensaje,
                             @AuthenticationPrincipal UserDetails userDetails) {

        return mensajeService.newMensaje(id, mensaje, userDetails);
    }

    @PostMapping("/hilo/edit-mensaje/{id}")
    public String editMensaje(@PathVariable Long id,
                              @RequestParam String contenido,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        return mensajeService.editMensaje(id, contenido, userDetails, redirectAttributes);
    }

    @PostMapping("/hilo/delete-mensaje/{id}")
    public String deleteMensaje(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {

        return mensajeService.deleteMensaje(id, userDetails, redirectAttributes);
    }

    @PostMapping("/hilo/{id}/like")
    public String toggleLike(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {

        return hiloService.likeHilo(id, userDetails);
    }
}
