package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String correo){
        return usuarioRepository.findByCorreoEquals(correo);
    }

    public Optional<Usuario> findByUsername(String username){
        return usuarioRepository.findByUsername(username);
    }

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void delete(Usuario usuario){
        usuarioRepository.delete(usuario);
    }
}
