package com.example.demo.Funcionalidades.Consultas;

import com.example.demo.Entidades.Carnet;
import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.Mongo.ConexionMongo;
import com.example.demo.Mongo.mongoDao.TorneoDao;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Consultas {

    EntrenadorService entrenadorService;
    CarnetService carnetService;

    public Consultas(EntrenadorService entrenadorService, CarnetService carnetService) {
        this.entrenadorService = entrenadorService;
        this.carnetService = carnetService;
    }

    public void datosTorneo() {
        Scanner sc = new Scanner(System.in);
        System.out.println("dame un nombre");
        String nombreTor = sc.nextLine();

        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase mongoDatabase = conexionMongo.getDatabase();
        TorneoDao torneoDao = new TorneoDao(mongoDatabase);

        Torneo torneo = torneoDao.obtenerTorneoPorNombre(nombreTor);
        if (torneo != null) {
            System.out.println(torneo.toString());
            System.out.println("quieres mas datos del torneo???");
            String siNo = sc.nextLine();
            if (siNo.equalsIgnoreCase("si") || siNo.equalsIgnoreCase("s")) {
                List<Entrenador> listaEntrenadores = new ArrayList<>(torneo.getEntrenadores());
                System.out.println("estos son los entrenadores:");
                for (Entrenador e : listaEntrenadores) {
                    System.out.println(e.getNombre());
                }
                List<Combate> listaCombates = new ArrayList<>(torneo.getCombates());
                System.out.println("el id de los combates son:");
                for (Combate c : listaCombates) {
                    System.out.println(c.getId());
                }
            }
        }
    }

    public void ganador() {
        Scanner sc = new Scanner(System.in);
        System.out.println("nombre del torneo para ver quien gano");
        String nombreTor = sc.nextLine();

        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase mongoDatabase = conexionMongo.getDatabase();
        TorneoDao torneoDao = new TorneoDao(mongoDatabase);

        Torneo torneo = torneoDao.obtenerTorneoPorNombre(nombreTor);
        if (torneo != null) {
            Entrenador entrenador = entrenadorService.buscarEntrenadorPorId(torneo.getGanadorTorneo());
            System.out.println(entrenador.toString());
        } else {
            System.out.println("no existe ese torneo");
        }
    }

    public void ganadorDosTorneos() {
        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase mongoDatabase = conexionMongo.getDatabase();
        TorneoDao torneoDao = new TorneoDao(mongoDatabase);

        List<Entrenador> entrenadores = torneoDao.obtenerEntrenadoresConMasDeDosVictorias();
        if (entrenadores != null) {
            System.out.println("los entrenadores son estos: ");
            for (Entrenador e : entrenadores) {
                System.out.println(e.getNombre());
            }
        } else {
            System.out.println("no existe ese tipo de entrenador");
        }
    }

    public void entrenadoresPuntos() {
        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase mongoDatabase = conexionMongo.getDatabase();
        TorneoDao torneoDao = new TorneoDao(mongoDatabase);

        List<Torneo> torneos = torneoDao.obtenerTodosLosTorneos();
        if (torneos != null) {
            for (Torneo torneo : torneos) {
                List<Entrenador> listaEntrenadores = new ArrayList<>(torneo.getEntrenadores());
                System.out.println("los entrenadores son " + torneo.getNombre() + " y tienen estos puntos");
                for (Entrenador e : listaEntrenadores) {
                    Carnet carnet = carnetService.buscarCarnetPorId(e.getId());
                    System.out.println(e.getNombre() + " -" + carnet.getPuntos());
                }
            }
        } else {
            System.out.println("no hay torneos");
        }
    }

    public void entrenadorPuntos() {
        Scanner sc = new Scanner(System.in);
        System.out.println("dame un nombre de un entrenador para ver los puntos");
        String nombreEntrenador = sc.nextLine();

        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase mongoDatabase = conexionMongo.getDatabase();
        TorneoDao torneoDao = new TorneoDao(mongoDatabase);

        Set<Entrenador> entrenadoresSet = torneoDao.obtenerTodosLosEntrenadores();
        List<Entrenador> entrenadoresList = new ArrayList<>(entrenadoresSet);
        boolean encontrado = false;
        long idEntrenador = 0;

        for (Entrenador e : entrenadoresList) {
            if (e.getNombre().equalsIgnoreCase(nombreEntrenador)) {
                idEntrenador = e.getId();
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            Carnet carnet = carnetService.buscarCarnetPorId(idEntrenador);
            System.out.println(nombreEntrenador + " tiene " + carnet.getPuntos() + " puntos");
        } else {
            System.out.println(nombreEntrenador + " no existe");
        }
    }

    public void region() {
        Scanner sc = new Scanner(System.in);
        System.out.println("dame una region(es una letra)");
        String regionInput = sc.nextLine();

        ConexionMongo conexionMongo = new ConexionMongo();
        MongoDatabase mongoDatabase = conexionMongo.getDatabase();
        TorneoDao torneoDao = new TorneoDao(mongoDatabase);

        List<Torneo> torneos = torneoDao.obtenerTodosLosTorneos();

        for (Torneo torneo : torneos) {
            if (String.valueOf(torneo.getCodRegion()).equalsIgnoreCase(regionInput.substring(0, 1))) {
                System.out.println("este es el torneo " + torneo.getNombre());
            }
        }
    }
}
