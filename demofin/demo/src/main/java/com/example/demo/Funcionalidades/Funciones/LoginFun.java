package com.example.demo.Funcionalidades.Funciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginFun {

    public static String buscarNombreYContraseñaYSacarId(String nombreFichero, String nombreBuscado, String contraseñaBuscada) {
        String idEncontrado = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreFichero))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(" ");
                if (partes.length >= 3) {
                    String nombre = partes[0];
                    String contraseña = partes[1];
                    String id = partes[3];
                    if (nombre.equalsIgnoreCase(nombreBuscado) && contraseña.equals(contraseñaBuscada)) {
                        idEncontrado = id;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("no se puede leer" + e.getMessage());
        }
        return idEncontrado;
    }
}
