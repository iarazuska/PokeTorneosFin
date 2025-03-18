package com.example.demo.Mongo.mongoDao;


import com.example.demo.Entidades.Combate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CombateDao {
    private MongoCollection<Document> combates;

    public CombateDao(MongoDatabase database) {
        this.combates = database.getCollection("combates");
    }

    public void guardarCombate(Combate combate) {
        Document doc = new Document("fecha", combate.getFecha())
                .append("torneo", combate.getTorneo().getId())
                .append("entrenador1", combate.getEntrenador1().getId())
                .append("entrenador2", combate.getEntrenador2().getId())
                .append("ganador", combate.getGanador());
        combates.insertOne(doc);
    }

    public Combate buscarCombatePorId(ObjectId id) {
        Document doc = combates.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            Date fechaDate = doc.getDate("fecha");  // Campo correcto en la BD
            LocalDate fecha = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            return new Combate(
                    doc.getLong("_id"),
                    fecha,  
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    public void eliminarCombate(ObjectId id) {
        combates.deleteOne(Filters.eq("_id", id));
    }
}
