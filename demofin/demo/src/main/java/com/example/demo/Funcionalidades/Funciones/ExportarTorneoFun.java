package com.example.demo.Funcionalidades.Funciones;

import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportarTorneoFun {

    public static void escribirEnArchivo(String nombreArchivo, Torneo torneo, List<Entrenador> entrenadores, List<Combate> combates) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            writer.write("torneo" + torneo.toString());
            writer.newLine();

            if (!entrenadores.isEmpty()) {
                writer.write("entrenadores");
                writer.newLine();
                for (Entrenador entrenador : entrenadores) {
                    writer.write(entrenador.toString());
                    writer.newLine();
                }
            }

            if (!combates.isEmpty()) {
                writer.write("combates");
                writer.newLine();
                for (Combate combate : combates) {
                    writer.write(combate.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("no se puede escribir " + e.getMessage());
        }
    }
}
