package com.example.demo.Funcionalidades.Gestion;

import java.util.Scanner;

public class Menu {

    public void menuGestion() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("que quires hacer????");
            System.out.println("1 listar Entrenadores y Administradores\n" +
                    "2 eliminar credenciales de entrenadores y administradores\n" +
                    "3 modificar contraseña de entrenadores y administradores");

            int num = sc.nextInt();

            Acciones acciones = new Acciones();

            switch (num) {
                case 1:
                    acciones.listarAdminEntre();
                    break;
                case 2:
                    acciones.eliminarUsu();
                    break;
                case 3:
                    acciones.modificarContra();
                    break;
                default:
                    System.out.println("pon un numero");
                    break;
            }
            break;
        }
    }
}
