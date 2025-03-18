package com.example.demo.Funcionalidades.Funciones;

import com.example.demo.Entidades.Carnet;
import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.Mongo.ConexionMongo;
import com.example.demo.Mongo.mongoDao.TorneoDao;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;
import com.mongodb.client.MongoDatabase;
import java.util.*;

public class Pelear {

    private CombateService combateService;
    private TorneoService torneoService;
    private CarnetService carnetService;
    private EntrenadorService entrenadorService;

    public Pelear(CombateService combateService, TorneoService torneoService, CarnetService carnetService, EntrenadorService entrenadorService) {
        this.combateService = combateService;
        this.torneoService = torneoService;
        this.entrenadorService = entrenadorService;
        this.carnetService = carnetService;
    }

    public void pelear(String id) {
        Scanner sc = new Scanner(System.in);
        long id_Tor = Long.parseLong(id);

        Torneo torneo = torneoService.buscarTorneoPorId(id_Tor);
        if (torneo == null) {
            System.err.println("no se encuentra el torneo con ese id" + id_Tor);
            return;
        }

        System.out.println("info de torneo");
        System.out.println(torneo.toString());

        Set<Combate> combates = torneo.getCombates();
        if (combates == null || combates.isEmpty()) {
            System.out.println("no hay combates ");
            return;
        }
        List<Combate> listaCombates = new ArrayList<>(combates);

        if (listaCombates.get(0).getGanador() != null) {
            System.out.println("ya se pelearon");
            return;
        }

        if (todosCombatesConEntrenadores(listaCombates)) {
            System.out.println("combates llenos");
            System.out.println("y el ganador es........");

            Map<Long, Integer> victoriasPorEntrenador = new HashMap<>();
            Random random = new Random();

            for (Combate combate : listaCombates) {
                int ganadorAleatorio = random.nextInt(2);
                Entrenador ganador;
                if (ganadorAleatorio == 0) {
                    ganador = combate.getEntrenador1();
                    combate.setGanador(ganador.getId());
                    System.out.println("combate " + combate.getId() + " ganador " + ganador.getNombre());
                } else {
                    ganador = combate.getEntrenador2();
                    combate.setGanador(ganador.getId());
                    System.out.println("combate " + combate.getId() + " ganador " + ganador.getNombre());
                }

                victoriasPorEntrenador.put(ganador.getId(), victoriasPorEntrenador.getOrDefault(ganador.getId(), 0) + 1);

                Carnet carnetGanador = carnetService.buscarCarnetPorId(ganador.getId());
                int puntosTorneo = torneo.getPuntosVictoria();
                carnetGanador.setPuntos(carnetGanador.getPuntos() + puntosTorneo);
                carnetGanador.setNumVictorias(carnetGanador.getNumVictorias() + 1);
                carnetService.guardarCarnet(carnetGanador);
                combateService.guardarCombate(combate);
            }

            Entrenador ganadorTorneo = determinarGanadorTorneo(victoriasPorEntrenador);
            if (ganadorTorneo != null) {
                torneo.setGanadorTorneo(ganadorTorneo.getId());
            } else {
                System.out.println("fallo en el torneo");
            }

            ConexionMongo conexionMongo = new ConexionMongo();
            MongoDatabase mongoDatabase = conexionMongo.getDatabase();
            TorneoDao torneoDao = new TorneoDao(mongoDatabase);
            torneoDao.guardarTorneo(torneo);

            if (ganadorTorneo != null) {
                System.out.println("este es el ganador " + ganadorTorneo.getNombre());
            } else {
                System.out.println("no se puede dar el ganador");
            }
        } else {
            System.out.println("combates no llenos");
        }
    }

    public boolean todosCombatesConEntrenadores(List<Combate> combates) {
        for (Combate combate : combates) {
            if (combate.getEntrenador1() == null || combate.getEntrenador2() == null) {
                return false;
            }
        }
        return true;
    }

    private Entrenador determinarGanadorTorneo(Map<Long, Integer> victoriasPorEntrenador) {
        Entrenador ganador = null;
        int maxVictorias = 0;

        for (Map.Entry<Long, Integer> entry : victoriasPorEntrenador.entrySet()) {
            if (entry.getValue() > maxVictorias) {
                maxVictorias = entry.getValue();
                ganador = entrenadorService.buscarEntrenadorPorId(entry.getKey());
            }
        }
        return ganador;
    }
}
