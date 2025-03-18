package com.example.demo.Funcionalidades.Crear;

import com.example.demo.DB4O.Usuario;
import com.example.demo.DB4O.UsuariosDb4o;
import com.example.demo.Entidades.Carnet;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.Funcionalidades.Funciones.EntrenadorFun;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class NuevoEntrenador {

    EntrenadorService entrenadorService;
    TorneoService torneoService;
    CarnetService carnetService;

    public NuevoEntrenador(TorneoService torneoService, EntrenadorService entrenadorService, CarnetService carnetService) {
        this.torneoService = torneoService;
        this.entrenadorService = entrenadorService;
        this.carnetService = carnetService;
    }

    public void crearEntrenador() throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean datosCorrectos = false;
        String nombre = null;
        String nacionalidad = null;
        String contra = null;

        do {
            System.out.print("como te llamas ");
            nombre = sc.next();
            System.out.print("tu contraseña ");
            contra = sc.next();
            System.out.println("elije una nacionalidad");
            List<String> listaPaises = EntrenadorFun.cargarNombresDePaises("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\paises.xml");
            System.out.println(listaPaises);

            nacionalidad = sc.next();

            boolean paisValido = false;
            do {
                if (EntrenadorFun.verificarPais(nacionalidad, listaPaises)) {
                    paisValido = true;
                } else {
                    System.out.println("mal");
                    System.out.println(listaPaises);
                    System.out.print("ponla bien");
                    nacionalidad = sc.next();
                }
            } while (!paisValido);

            System.out.println("nombre" + nombre +
                    "\ncontraseña " + contra +
                    "\nnacionalidad" + nacionalidad);
            System.out.println("si/no");
            String validar = sc.next();

            if (validar.equalsIgnoreCase("si") || validar.equalsIgnoreCase("s")) {
                datosCorrectos = true;
            }
        } while (!datosCorrectos);

        EntrenadorFun metodosCrear = new EntrenadorFun();
        long id = metodosCrear.obtenerUltimoIdEntrenador("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", "entrenador");
        if (id == 0) {
            id = 1;
        }
        id += 1;

        if (!EntrenadorFun.verificarCredenciales("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", nombre, contra, "entrenador")) {

            System.out.print("en que torneo te quieres meter");
            System.out.println(EntrenadorFun.obtenerNombresDesdeArchivo("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\torneo.dat"));
            String torneo = sc.next();

            if (EntrenadorFun.buscarNombreTorneo("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\torneo.dat", torneo)) {
                System.out.println("entrenador creado");

                long idTorneo = metodosCrear.sacarIdTorneo("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\torneo.dat", torneo);

                Torneo torneoBD = torneoService.buscarTorneoPorId(idTorneo);
                Set<Entrenador> entrenadorSet = torneoBD.getEntrenadores();

                int numEntre = entrenadorSet.size();
                if (numEntre < 3) {
                    Entrenador entrenadorNuevo = new Entrenador();
                    entrenadorNuevo.setNombre(nombre);
                    entrenadorNuevo.setNacionalidad(nacionalidad);
                    entrenadorNuevo.agregarTorneo(torneoBD);

                    Carnet carnet = new Carnet();
                    carnet.setEntrenador(entrenadorNuevo);
                    carnet.setIdEntrenador(entrenadorNuevo.getId());
                    carnet.setFechaExpedicion(LocalDate.now());
                    carnet.setNumVictorias(0);
                    entrenadorNuevo.setCarnet(carnet);

                    carnetService.guardarCarnet(carnet);

                    metodosCrear.escribirAlFinalDelTxtEntrenador("C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.txt", entrenadorNuevo, contra);

                    Usuario usuario = new Usuario(nombre, contra, "entrenador", entrenadorNuevo.getId());
                    UsuariosDb4o usuariosDb4o = new UsuariosDb4o();
                    usuariosDb4o.nuevo(usuario);

                    System.out.println("todo guardado en todos lados");
                } else {
                    System.out.println("torneo lleno");
                }
            } else {
                System.out.println("ese torneo no existe");
                System.out.println("entrenador no creado");
            }
        } else {
            System.out.println("ya estas en la base de datos");
        }
    }
}
