package com.colibri.toread.persistence;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.colibri.toread.ReadProperties;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class Persistence {
	private Morphia morphia;
	private MongoClient mongoClient;
	
	private static int port;
	private static String host;
	
	private static void initMongo() throws UnknownHostException {
		port = Integer.getInteger(ReadProperties.getProperty("mongoport"));
		host = ReadProperties.getProperty("dbserver");
		
		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress(host, port)));
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongoClient, "ToRead");
		System.out.println("Persistent initialised");
	}
}
