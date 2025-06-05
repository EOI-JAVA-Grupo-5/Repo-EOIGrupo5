package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.RelacionUsuarios;
import com.eoi.grupo5.repositories.RelacionesUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelacionesUsuarioService {
    RelacionesUsuarioRepository relacionesUsuarioRepository;

    public RelacionesUsuarioService(RelacionesUsuarioRepository relacionesUsuarioRepository){
        this.relacionesUsuarioRepository = relacionesUsuarioRepository;
    }

    public List<RelacionUsuarios> findAll(){
        return relacionesUsuarioRepository.findAll();
    }

    public Optional<RelacionUsuarios> findById(Long id){
        return relacionesUsuarioRepository.findById(id);
    }

    public RelacionUsuarios save(RelacionUsuarios relacionUsuarios){
        return relacionesUsuarioRepository.save(relacionUsuarios);
    }

    public void delete(RelacionUsuarios relacionUsuarios){
        relacionesUsuarioRepository.delete(relacionUsuarios);
    }
}
