package com.example.demo;

import com.example.demo.Funcionalidades.Inicio.Iniciar;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	TorneoService service;

	@Autowired
	EntrenadorService entrenadorService;

	@Autowired
	CombateService combateService;


	@Autowired
	CarnetService carnetService;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Iniciar iniciar = new Iniciar(service,entrenadorService,combateService,carnetService);
		iniciar.mostrarMenu();

	}
}