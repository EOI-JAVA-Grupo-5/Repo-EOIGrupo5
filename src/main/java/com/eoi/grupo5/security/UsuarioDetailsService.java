package com.eoi.grupo5.security;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String credencialUsuario) throws UsernameNotFoundException {

        Usuario usuario;

//        if(credencialUsuario.contains("@")){
//            usuario = usuarioRepository.findByCorreoEquals(credencialUsuario)
//                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//        }else{
            usuario = usuarioRepository.findByUsername(credencialUsuario)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//        }


        log.info("+++++++++++++++ USUARIO " + usuario.getUsername());
        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getTipo().toString())
                .build();
    }
}
