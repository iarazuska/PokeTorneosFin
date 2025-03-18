package com.example.demo.Mongo.mongoDao;

import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class TorneoDao {
    private MongoCollection<Document> torneos;

    public TorneoDao(MongoDatabase database) {
        this.torneos = database.getCollection("datos");
    }

    public void guardarTorneo(Torneo torneo) {
        Document torneoDoc = new Document("nombre", torneo.getNombre())
                .append("codRegion", torneo.getCodRegion())
                .append("puntosVictoria", torneo.getPuntosVictoria())
                .append("ganadorTorneo", torneo.getGanadorTorneo());

        List<Document> entrenadoresDocs = new ArrayList<>();
        for (Entrenador entrenador : torneo.getEntrenadores()) {
            Document entrenadorDoc = new Document("id", entrenador.getId())
                    .append("nombre", entrenador.getNombre());
            entrenadoresDocs.add(entrenadorDoc);
        }
        torneoDoc.append("entrenadores", entrenadoresDocs);

        List<Document> combatesDocs = new ArrayList<>();
        for (Combate combate : torneo.getCombates()) {
            Document combateDoc = new Document("id", combate.getId())
                    .append("resultado", combate.getGanador())
                    .append("entrenador1", combate.getEntrenador1().getId())
                    .append("entrenador2", combate.getEntrenador2().getId());
            combatesDocs.add(combateDoc);
        }
        torneoDoc.append("combates", combatesDocs);

        torneos.insertOne(torneoDoc);
    }

    public List<Entrenador> obtenerEntrenadoresConMasDeDosVictorias() {
        Map<Long, Integer> contadorVictorias = new HashMap<>();

        for (Document torneoDoc : torneos.find()) {
            Long ganadorId = torneoDoc.getLong("ganadorTorneo");
            if (ganadorId != null) {
                contadorVictorias.put(ganadorId, contadorVictorias.getOrDefault(ganadorId, 0) + 1);
            }
        }

        List<Entrenador> entrenadoresGanadores = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : contadorVictorias.entrySet()) {
            if (entry.getValue() > 2) {
                for (Document torneoDoc : torneos.find()) {
                    List<Document> entrenadoresDocs = torneoDoc.getList("entrenadores", Document.class);
                    if (entrenadoresDocs != null) {
                        for (Document doc : entrenadoresDocs) {
                            if (doc.getLong("id").equals(entry.getKey())) {
                                Entrenador entrenador = new Entrenador();
                                entrenador.setId(doc.getLong("id"));
                                entrenador.setNombre(doc.getString("nombre"));
                                entrenadoresGanadores.add(entrenador);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return entrenadoresGanadores;
    }

    public Set<Entrenador> obtenerTodosLosEntrenadores() {
        Set<Entrenador> entrenadores = new HashSet<>();
        for (Document torneoDoc : torneos.find()) {
            List<Document> entrenadoresDocs = torneoDoc.getList("entrenadores", Document.class);
            if (entrenadoresDocs != null) {
                for (Document doc : entrenadoresDocs) {
                    Entrenador entrenador = new Entrenador();
                    entrenador.setId(doc.getLong("id"));
                    entrenador.setNombre(doc.getString("nombre"));
                    entrenadores.add(entrenador);
                }
            }
        }
        return entrenadores;
    }

    public List<Torneo> obtenerTodosLosTorneos() {
        List<Torneo> listaTorneos = new ArrayList<>();
        for (Document torneoDoc : torneos.find()) {
            Torneo torneo = new Torneo();
            torneo.setNombre(torneoDoc.getString("nombre"));
            torneo.setCodRegion(torneoDoc.getString("codRegion").charAt(0));
            torneo.setPuntosVictoria(torneoDoc.getInteger("puntosVictoria"));
            torneo.setGanadorTorneo(torneoDoc.getLong("ganadorTorneo"));

            List<Document> entrenadoresDocs = torneoDoc.getList("entrenadores", Document.class);
            Set<Entrenador> entrenadores = new HashSet<>();
            if (entrenadoresDocs != null) {
                for (Document doc : entrenadoresDocs) {
                    Entrenador entrenador = new Entrenador();
                    entrenador.setId(doc.getLong("id"));
                    entrenador.setNombre(doc.getString("nombre"));
                    entrenadores.add(entrenador);
                }
            }
            torneo.setEntrenadores(entrenadores);

            List<Document> combatesDocs = torneoDoc.getList("combates", Document.class);
            Set<Combate> combates = new HashSet<>();
            if (combatesDocs != null) {
                for (Document doc : combatesDocs) {
                    Combate combate = new Combate();
                    combate.setId(doc.getLong("id"));
                    combate.setGanador(doc.getLong("resultado"));

                    Entrenador entrenador1 = new Entrenador();
                    entrenador1.setId(doc.getLong("entrenador1"));

                    Entrenador entrenador2 = new Entrenador();
                    entrenador2.setId(doc.getLong("entrenador2"));

                    combate.setEntrenador1(entrenador1);
                    combate.setEntrenador2(entrenador2);

                    combates.add(combate);
                }
            }
            torneo.setCombates(combates);

            listaTorneos.add(torneo);
        }
        return listaTorneos;
    }

    public Torneo obtenerTorneoPorNombre(String nombre) {
        Document torneoDoc = torneos.find(Filters.eq("nombre", nombre)).first();

        if (torneoDoc == null) {
            System.out.println("No se encontró un torneo con el nombre: " + nombre);
            return null;
        }

        Torneo torneo = new Torneo();
        torneo.setNombre(torneoDoc.getString("nombre"));
        torneo.setCodRegion(torneoDoc.getString("codRegion").charAt(0));
        torneo.setPuntosVictoria(torneoDoc.getInteger("puntosVictoria"));
        torneo.setGanadorTorneo(torneoDoc.getLong("ganadorTorneo"));

        List<Document> entrenadoresDocs = torneoDoc.getList("entrenadores", Document.class);
        Set<Entrenador> entrenadores = new HashSet<>();
        if (entrenadoresDocs != null) {
            for (Document doc : entrenadoresDocs) {
                Entrenador entrenador = new Entrenador();
                entrenador.setId(doc.getLong("id"));
                entrenador.setNombre(doc.getString("nombre"));
                entrenadores.add(entrenador);
            }
        }
        torneo.setEntrenadores(entrenadores);

        List<Document> combatesDocs = torneoDoc.getList("combates", Document.class);
        Set<Combate> combates = new HashSet<>();
        if (combatesDocs != null) {
            for (Document doc : combatesDocs) {
                Combate combate = new Combate();
                combate.setId(doc.getLong("id"));
                combate.setGanador(doc.getLong("resultado"));

                Entrenador entrenador1 = new Entrenador();
                entrenador1.setId(doc.getLong("entrenador1"));

                Entrenador entrenador2 = new Entrenador();
                entrenador2.setId(doc.getLong("entrenador2"));

                combate.setEntrenador1(entrenador1);
                combate.setEntrenador2(entrenador2);

                combates.add(combate);
            }
        }
        torneo.setCombates(combates);

        return torneo;
    }

    public Torneo buscarTorneoPorId(ObjectId id) {
        Document doc = torneos.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            return new Torneo(
                    doc.getString("nombre"),
                    doc.getString("codRegion").charAt(0),
                    doc.getInteger("puntosVictoria"),
                    null,
                    null,
                    doc.getLong("ganadorTorneo")
            );
        }
        return null;
    }

    public void eliminarTorneo(ObjectId id) {
        torneos.deleteOne(Filters.eq("_id", id));
    }
}
