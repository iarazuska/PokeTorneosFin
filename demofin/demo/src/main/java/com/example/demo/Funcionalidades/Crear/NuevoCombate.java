package com.example.demo.Funcionalidades.Crear;

import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;

import java.util.*;
import java.util.stream.Collectors;

public class NuevoCombate {

    private CarnetService carnetService;
    private CombateService combateService;
    private TorneoService torneoService;
    private EntrenadorService entrenadorService;

    public NuevoCombate(CombateService combateService, CarnetService carnetService, TorneoService torneoService, EntrenadorService entrenadorService) {
        this.carnetService = carnetService;
        this.combateService = combateService;
        this.torneoService = torneoService;
        this.entrenadorService = entrenadorService;
    }

    public void inscribir(String id) {
        Scanner sc = new Scanner(System.in);

        long id_Tor = Long.parseLong(id);
        Torneo torneo = torneoService.buscarTorneoPorId(id_Tor);

        if (torneo == null) {
            System.err.println("no se encontro el id " + id_Tor);
            return;
        }

        System.out.println("esta es la info");
        System.out.println(torneo.toString());

        Set<Combate> combates = torneo.getCombates();
        if (combates == null || combates.isEmpty()) {
            System.out.println("no hay combates");
            return;
        }
        List<Combate> listaCombates = new ArrayList<>(combates);

        Set<Entrenador> entrenadorSet = torneo.getEntrenadores();
        if (entrenadorSet == null) {
            entrenadorSet = new HashSet<>();
            torneo.setEntrenadores(entrenadorSet);
        }
        List<Entrenador> listaEntrenadores = new ArrayList<>(entrenadorSet);

        if (listaCombates.size() > 1 && listaCombates.get(1).getGanador() != null) {
            System.out.println("torneo acabado ");
            return;
        }

        while (listaEntrenadores.size() < 3) {
            System.out.println("torneo no lleno");
            System.out.println("puede poner a estos entrenadores");

            List<Entrenador> todosLosEntrenadores = entrenadorService.obtenerTodosLosEntrenadores();

            List<Entrenador> entrenadoresNoInscritos = todosLosEntrenadores.stream()
                    .filter(e -> !listaEntrenadores.contains(e))
                    .collect(Collectors.toList());

            if (entrenadoresNoInscritos.isEmpty()) {
                System.out.println("no hay entrenadores");
                return;
            }

            for (int i = 0; i < entrenadoresNoInscritos.size(); i++) {
                System.out.println((i + 1) + ". " + entrenadoresNoInscritos.get(i).getNombre());
            }

            System.out.print("quieres meter a este entrenador???(si/no)");
            String respuesta = sc.nextLine().trim().toLowerCase();

            if (respuesta.equals("si") || respuesta.equals("s")) {
                System.out.print("pon el nombre del entrenador ");
                String nombreNuevoEntrenador = sc.nextLine();

                Entrenador nuevoEntrenador = entrenadoresNoInscritos.stream()
                        .filter(e -> e.getNombre().equalsIgnoreCase(nombreNuevoEntrenador))
                        .findFirst()
                        .orElse(null);

                if (nuevoEntrenador != null) {
                    listaEntrenadores.add(nuevoEntrenador);
                    torneo.getEntrenadores().add(nuevoEntrenador);
                    torneoService.guardarTorneo(torneo);
                    entrenadorService.guardarEntrenador(nuevoEntrenador);
                    System.out.println(nuevoEntrenador.getNombre() + " listo");
                } else {
                    System.out.println("el entrenador no esta en la lista");
                }
            } else {
                return;
            }
        }

        if (listaEntrenadores.size() < 3) {
            System.out.println("no hay entrenadores para los combates");
            return;
        }

        listaCombates.sort(Comparator.comparingLong(Combate::getId));

        if (listaCombates.size() < 3) {
            System.out.println("no hay combates");
            return;
        }

        Combate combate1 = listaCombates.get(0);
        Combate combate2 = listaCombates.get(1);
        Combate combate3 = listaCombates.get(2);

        Entrenador entrenador1 = listaEntrenadores.get(0);
        Entrenador entrenador2 = listaEntrenadores.get(1);
        Entrenador entrenador3 = listaEntrenadores.get(2);

        combate1.setEntrenador1(entrenador1);
        combate1.setEntrenador2(entrenador2);

        combate2.setEntrenador1(entrenador1);
        combate2.setEntrenador2(entrenador3);

        combate3.setEntrenador1(entrenador2);
        combate3.setEntrenador2(entrenador3);

        combateService.guardarCombate(combate1);
        combateService.guardarCombate(combate2);
        combateService.guardarCombate(combate3);

        System.out.println("entrenadores en los combates");
    }
}
