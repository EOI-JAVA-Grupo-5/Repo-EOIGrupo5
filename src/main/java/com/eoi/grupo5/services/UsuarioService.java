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

    /**
     * Busca todos los usuarios en la base de datos
     * @return Lista con todos los usuarios
     */
    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su id
     * @param id - ID del usuario
     * @return Optional del usuario buscado
     */
    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su correo electrónico
     * @param correo - Correo del usuario
     * @return Optional del usuario buscado
     */
    public Optional<Usuario> findByEmail(String correo){
        return usuarioRepository.findByCorreoEquals(correo);
    }

    /**
     * Encuentra al usuario por su nombre
     * @param username - Nombre de usuario
     * @return Optional del usuario buscado
     */
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