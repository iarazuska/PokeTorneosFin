package com.example.demo.Funcionalidades.Crear;

import com.example.demo.DB4O.Usuario;
import com.example.demo.DB4O.UsuariosDb4o;
import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Torneo;
import com.example.demo.Funcionalidades.Funciones.TorneoFun;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.TorneoService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class NuevoTorneo {

    TorneoService torneoService;
    CombateService combateService;

    public NuevoTorneo(TorneoService service, CombateService combateService) {
        this.torneoService = service;
        this.combateService = combateService;
    }

    public void crearTorneo() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("nombre torneo");
        String nombreTor = sc.next();
        System.out.print("localidad(una letra)");
        String localidad = sc.next();

        TorneoFun torneoFun = new TorneoFun();

        if (!torneoFun.existeTorneo("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\torneo.dat", nombreTor, localidad)) {
            System.out.println("quien va a controlar el torneo");

            boolean credencialesValidas = false;
            String usu = null;
            String contra = null;
            do {
                System.out.print("nombre");
                usu = sc.next();
                System.out.print("contraña");
                contra = sc.next();
                System.out.println("nombre" + usu + "\ncontraseña " + contra);
                System.out.println("si/no");
                String validar = sc.next();

                if (validar.equalsIgnoreCase("si") || validar.equalsIgnoreCase("s")) {
                    credencialesValidas = true;
                }
            } while (!credencialesValidas);

            int id_Todo = torneoFun.obtenerUltimoIdTorneo("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\torneo.dat");
            id_Todo += 1;

            if (credencialesValidas) {
                TorneoFun admin = new TorneoFun(usu, contra, "torneo", id_Todo);

                if (!admin.existeEnFichero("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", "torneo")) {

                    Torneo torneo = new Torneo();
                    torneo.setNombre(nombreTor);
                    torneo.setPuntosVictoria(100);
                    torneo.setCodRegion(localidad.charAt(0));

                    torneoService.guardarTorneo(torneo);
                    Torneo torneoCreado = torneoService.buscarTorneoPorId(torneo.getId());

                    torneoFun.guardarSiNoExisteConDatos("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\torneo.dat", torneoCreado, usu);
                    admin.guardarSiNoExiste("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", "torneo", torneoCreado.getId());
                    System.out.println("torneo listo");
                    System.out.println("vamos a cear los combates ");

                    Usuario usuario = new Usuario(usu, contra, "torneo", torneo.getId());
                    UsuariosDb4o usuariosDb4o = new UsuariosDb4o();
                    usuariosDb4o.nuevo(usuario);

                    LocalDate[] fechas = new LocalDate[3];
                    boolean fechaValida;

                    for (int i = 0; i < 3; i++) {
                        fechaValida = false;
                        while (!fechaValida) {
                            System.out.print("fecha" + (i + 1) + " de este modo(yyyy-mm-dd): ");
                            String fechaStr = sc.next();
                            try {
                                fechas[i] = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
                                fechaValida = true;
                            } catch (DateTimeParseException e) {
                                System.out.println("fecha mal puesta");
                            }
                        }
                    }

                    for (LocalDate fecha : fechas) {
                        Combate combate = new Combate();
                        combate.setTorneo(torneoCreado);
                        combate.setFecha(fecha);
                        combateService.guardarCombate(combate);
                    }
                    System.out.println("combates guardados");

                } else {
                    System.out.println("este administrador ya exixte");
                    System.out.println("torneo no creado");
                }
            }
        } else {
            System.out.println("este torneo ya exixte no se puden duplicados");
        }
    }
}
