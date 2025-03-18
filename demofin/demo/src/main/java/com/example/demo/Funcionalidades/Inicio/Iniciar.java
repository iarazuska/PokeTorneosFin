package com.example.demo.Funcionalidades.Inicio;

import com.example.demo.Funcionalidades.Crear.NuevoEntrenador;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Scanner;

@Service
public class Iniciar {

    TorneoService torneoService;
    EntrenadorService entrenadorService;
    CombateService combateRepository;
    CarnetService carnetService;

    public Iniciar(TorneoService torneoService, EntrenadorService entrenadorService, CombateService combateRepository, CarnetService carnetService) {
        super();
        this.torneoService = torneoService;
        this.entrenadorService = entrenadorService;
        this.combateRepository = combateRepository;
        this.carnetService = carnetService;
    }

    public void mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        do {
            System.out.println("menu invitado");
            System.out.println(
                    "1 iniciar sesion\n" +
                            "2 nuevo entrenador\n" +
                            "3 salir\n" +
                            "que quieres hacer???");

            if (!sc.hasNextInt()) {
                System.out.println("pon un numero que este");
                sc.next();
                continue;
            }

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    Login login = new Login(torneoService, combateRepository, entrenadorService, carnetService);
                    try {
                        login.IniciarSesion();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    NuevoEntrenador nuevoEntrenador = new NuevoEntrenador(torneoService, entrenadorService, carnetService);
                    try {
                        nuevoEntrenador.crearEntrenador();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("adios");
                    salir = true;
                    break;
                default:
            }
        } while (!salir);
    }
}
