package com.example.demo.SpreenBot.service;


import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.SpreenBot.repository.EntrenadorRepository;
import com.example.demo.SpreenBot.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntrenadorService {

    @Autowired
    private EntrenadorRepository entrenadorRepository;
    
    @Autowired
    private TorneoRepository torneoRepository;

    @Transactional
    public Entrenador guardarEntrenador(Entrenador entrenador) {
        // Sincroniza las relaciones bidireccionales
        if (entrenador.getTorneos() != null) {
            for (Torneo torneo : entrenador.getTorneos()) {
                torneo.agregarEntrenador(entrenador);
            }
        }

        // Guarda el entrenador
        return entrenadorRepository.save(entrenador);
    }

    public List<Entrenador> obtenerTodosLosEntrenadores() {
        return entrenadorRepository.findAll();
    }
    
    
    public Entrenador buscarEntrenadorPorId(Long id) {
        return entrenadorRepository.findById(id).orElse(null);
    }
}
