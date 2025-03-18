package com.example.demo.Funcionalidades.Inicio;

import com.example.demo.DB4O.Usuario;
import com.example.demo.DB4O.UsuariosDb4o;
import com.example.demo.Funcionalidades.Consultas.ConsultasTorneo;
import com.example.demo.Funcionalidades.Crear.NuevoCombate;
import com.example.demo.Funcionalidades.Crear.NuevoTorneo;
import com.example.demo.Funcionalidades.Exportar.ExportarCarnet;
import com.example.demo.Funcionalidades.Exportar.ExportarTorneo;
import com.example.demo.Funcionalidades.Funciones.LoginFun;
import com.example.demo.Funcionalidades.Funciones.Pelear;
import com.example.demo.Funcionalidades.Gestion.Menu;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;

import java.sql.SQLException;
import java.util.Scanner;

public class Login {

    TorneoService service;
    CombateService combateService;
    EntrenadorService entrenadorService;
    CarnetService carnetService;

    public Login(TorneoService service, CombateService combateService, EntrenadorService entrenadorService, CarnetService carnetService) {
        this.service = service;
        this.combateService = combateService;
        this.entrenadorService = entrenadorService;
        this.carnetService = carnetService;
    }

    public void IniciarSesion() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("quien eres ");
        String usu = sc.next();
        System.out.print("contra ");
        String contra = sc.next();

        if (usu.equalsIgnoreCase("iarazas") && contra.equals("1234")) {
            while (true) {
                System.out.println("menu admin general");
                System.out.println("1 crear un nuevo Torneo\n" +
                        "2 consultas Torneos \n" +
                        "3 gestión de Usuarios\n"+
                        "4 salir");
                int num = sc.nextInt();
                switch (num) {
                    case 1:
                        NuevoTorneo nuevoTorneo = new NuevoTorneo(service, combateService);
                        nuevoTorneo.crearTorneo();
                        break;
                    case 2:
                        ConsultasTorneo consultasTorneo = new ConsultasTorneo(entrenadorService,carnetService);
                        consultasTorneo.consultas();
                        break;
                    case 3:
                        Menu menu = new Menu();
                        menu.menuGestion();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("pon un numero que este ");
                        break;
                }
            }
        }
        UsuariosDb4o usuariosDb4o = new UsuariosDb4o();
        Usuario usuario = usuariosDb4o.buscarUsuarioPorNombreYContraseña(usu, contra);
        if (usuario != null){
            while (true) {
                switch (usuario.getPerfil()) {
                    case "torneo":
                        String i = LoginFun.buscarNombreYContraseñaYSacarId("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", usu, contra);
                        System.out.println("menu admin torneo");
                        System.out.println("1 exportar Datos de Torneo\n" +
                                "2 nuevo combate\n" +
                                "3 pelear\n"+
                                "4 salir");
                        int num1 = sc.nextInt();
                        switch (num1) {
                            case 1:
                                ExportarTorneo exportarTorneo = new ExportarTorneo(service,entrenadorService,combateService);
                                exportarTorneo.exportarTorneo(i);
                                break;
                            case 2:
                                NuevoCombate nuevoCombate = new NuevoCombate(combateService ,carnetService,service,entrenadorService);
                                nuevoCombate.inscribir(i);
                                break;
                            case 3:
                                Pelear pelear = new Pelear(combateService,service,carnetService,entrenadorService);
                                pelear.pelear(i);
                                break;
                            case 4:
                                return;
                            default:
                                System.out.println("pon un numero que este ");
                                break;
                        }
                        break;

                    case "entrenador":
                        String id = LoginFun.buscarNombreYContraseñaYSacarId("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", usu, contra);
                        System.out.println("menu entrenador ");
                        System.out.println("1 exportar tu Carnet en XML\n" +
                                "2 salir");
                        int num2 = sc.nextInt();
                        switch (num2) {
                            case 1:
                                ExportarCarnet exportarCarnet = new ExportarCarnet(entrenadorService,carnetService,service,combateService);
                                exportarCarnet.exportarCarnet(id);
                                break;
                            case 2:
                                return;
                            default:
                                System.out.println("pon un numero que este");
                                break;
                        }
                        break;

                    default:
                        System.out.println("ese perfil no existe");
                        return;
                }
            }
        } else {
            System.out.println("datos mal puestos");
        }
    }
}
