package com.example.demo.Mongo.mongoDao;


import com.example.demo.Entidades.Carnet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CarnetDao {
    private MongoCollection<Document> carnets;

    public CarnetDao(MongoDatabase database) {
        this.carnets = database.getCollection("carnets");
    }

    public void guardarCarnet(Carnet carnet) {
        Document doc = new Document("id_entrenador", carnet.getIdEntrenador())
                .append("fechaExpedicion", carnet.getFechaExpedicion())
                .append("puntos", carnet.getPuntos())
                .append("numVictorias", carnet.getNumVictorias());
        carnets.insertOne(doc);
    }

    public Carnet buscarCarnetPorIdEntrenador(Long idEntrenador) {
        Document doc = carnets.find(Filters.eq("id_entrenador", idEntrenador)).first();
        if (doc != null) {
        	Date fechaExpedicionDate = doc.getDate("fechaExpedicion");
            LocalDate fechaExpedicion = fechaExpedicionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            return new Carnet(
                    doc.getLong("id_entrenador"),
                    fechaExpedicion,
                    doc.getInteger("puntos"),
                    doc.getInteger("numVictorias"),
                    null
            );
        }
        return null;
    }

    public void eliminarCarnet(Long idEntrenador) {
        carnets.deleteOne(Filters.eq("id_entrenador", idEntrenador));
    }
}
