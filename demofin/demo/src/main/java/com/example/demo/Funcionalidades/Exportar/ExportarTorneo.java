package com.example.demo.Funcionalidades.Exportar;

import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.Funcionalidades.Funciones.ExportarTorneoFun;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ExportarTorneo {

    TorneoService torneoService;
    EntrenadorService entrenadorService;
    CombateService combateService;

    public ExportarTorneo(TorneoService torneoService, EntrenadorService entrenadorService, CombateService combateService) {
        this.torneoService = torneoService;
        this.entrenadorService = entrenadorService;
        this.combateService = combateService;
    }

    public void exportarTorneo(String id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        long idTorneo = Long.parseLong(id);
        Torneo torneo = torneoService.buscarTorneoPorId(idTorneo);

        System.out.println("ifo del torneo");
        System.out.println(torneo.toString());

        Set<Entrenador> entrenadorSet = torneo.getEntrenadores();
        List<Entrenador> listaEntrenadores = new ArrayList<>(entrenadorSet);

        Set<Combate> combates = torneo.getCombates();
        List<Combate> listaCombates = new ArrayList<>(combates);

        System.out.println("entrenadores");
        if (entrenadorSet.isEmpty()) {
            System.out.println("no hay entrenadores");
        } else {
            System.out.println(entrenadorSet);
        }

        System.out.println("combates");
        if (listaCombates.isEmpty()) {
            System.out.println("no hay combates");
        } else {
            System.out.println(combates);
        }

        System.out.print("lo quieres guardar?????");
        String siNo = sc.next();
        if (siNo.equalsIgnoreCase("si") || siNo.equalsIgnoreCase("s")) {
            ExportarTorneoFun.escribirEnArchivo("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Torneos" + torneo.getNombre().toUpperCase() + ".txt",
                    torneo, listaEntrenadores, listaCombates);
        } else {
            System.out.println("no se puede exportar");
        }
    }
}
