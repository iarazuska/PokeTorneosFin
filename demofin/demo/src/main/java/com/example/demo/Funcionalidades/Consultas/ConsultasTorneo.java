package com.example.demo.Funcionalidades.Consultas;

import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Scanner;

public class ConsultasTorneo {

    @Autowired
    EntrenadorService entrenadorService;
    @Autowired
    CarnetService carnetService;

    public ConsultasTorneo(EntrenadorService entrenadorService, CarnetService carnetService) {
        this.entrenadorService = entrenadorService;
        this.carnetService = carnetService;
    }

    public void consultas() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("que quieres hacer");
            System.out.println("1 dar un nombre de un Torneo y sacar información\n" +
                    "2 dar un nombre de un Torneo y decirte qué entrenador lo ha ganado\n" +
                    "3 listar los 2 entrenadores que han ganado más torneos y cuántos ganó cada uno\n" +
                    "4 listar todos los entrenadores y cuántos puntos tiene cada uno\n" +
                    "5 dar un nombre de un Entrenador y decirte cuántos puntos tiene\n" +
                    "6 dar una región y decirte los Torneos que se han disputado\n" +
                    "7 salir");
            System.out.println("elije");
            int num = sc.nextInt();
            sc.nextLine();

            Consultas consultas = new Consultas(entrenadorService, carnetService);

            switch (num) {
                case 1:
                    consultas.datosTorneo();
                    break;
                case 2:
                    consultas.ganador();
                    break;
                case 3:
                    consultas.ganadorDosTorneos();
                    break;
                case 4:
                    consultas.entrenadoresPuntos();
                    break;
                case 5:
                    consultas.entrenadorPuntos();
                    break;
                case 6:
                    consultas.region();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("pon un numero que hay");
                    break;
            }
        }
    }
}
