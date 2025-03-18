package com.example.demo.Mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo {
	private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "poketorneos";

    private static MongoClient mongoClient;
    private static MongoDatabase database;


    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(DATABASE_NAME);

        }
        return database;
    }


    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println("conexion cerrada");
        }
    }
}
