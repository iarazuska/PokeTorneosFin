package com.example.demo.SpreenBot.service;


import com.example.demo.Entidades.Combate;
import com.example.demo.SpreenBot.repository.CombateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CombateService {

    @Autowired
    private CombateRepository combateRepository;

    public Combate guardarCombate(Combate combate) {
        return combateRepository.save(combate);
    }

    public Combate buscarCombatePorId(Long id) {
        return combateRepository.findById(id).orElse(null);
    }


}
