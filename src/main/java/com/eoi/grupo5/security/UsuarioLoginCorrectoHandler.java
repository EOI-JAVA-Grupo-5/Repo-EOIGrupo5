package com.eoi.grupo5.security;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.ListaRepository;
import com.eoi.grupo5.repositories.UsuarioRepository;
import com.eoi.grupo5.services.ListaService;
import com.eoi.grupo5.services.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UsuarioLoginCorrectoHandler implements AuthenticationSuccessHandler {

    @Autowired
    private final ListaService listaService;

    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioLoginCorrectoHandler(ListaService listaService, UsuarioService usuarioService) {
        this.listaService = listaService;
        this.usuarioService = usuarioService;
    }


    /**
     * Cuando un usuario inicia sesion correctamente, se comprueba si el usuario tiene una lista abierta.
     * Si no la tiene, el sistema le crea una nueva lista abierta.
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     *
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {



        String username = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));

        listaService.crearListaParaUsuario(usuario);

        response.sendRedirect("/usuario");

    }
}
