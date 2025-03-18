package com.example.demo.SpreenBot.service;


import com.example.demo.Entidades.Torneo;
import com.example.demo.SpreenBot.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TorneoService {

    @Autowired
    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository) {
		super();
		this.torneoRepository = torneoRepository;
	}

	public Torneo guardarTorneo(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    public Torneo buscarTorneoPorId(Long id) {
        return torneoRepository.findById(id).orElse(null);
    }


}