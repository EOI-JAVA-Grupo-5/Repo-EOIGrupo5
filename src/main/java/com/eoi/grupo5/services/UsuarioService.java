package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.Usuario;
import com.eoi.grupo5.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepo.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepo.findById(id);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public void borrarUsuario(Long id) {
        usuarioRepo.deleteById(id);
    }

    public boolean existeUsuario(Long id) {
        return usuarioRepo.existsById(id);
    }

    public Optional<Usuario> obtenerUsuarioPorNombreUsuario(String nombreUsuario) {
        return usuarioRepo.findUsuarioByNombreUsuario(nombreUsuario);
    }

}
