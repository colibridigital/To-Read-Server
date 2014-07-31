package com.colibri.toread.persistence;

import com.colibri.toread.ReadProperties;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public final class MongoConnectionManager {
	public static final MongoConnectionManager INSTANCE = new MongoConnectionManager();
	private Datastore db;
	
	private MongoConnectionManager() {
		int port = Integer.parseInt(ReadProperties.getProperty("mongoport"));
		String host = ReadProperties.getProperty("dbserver");
		
		try {
			Mongo m = new MongoClient(host, port);
			Morphia morphia = new Morphia();
			
			String classString = ReadProperties.getProperty("classmap");
			String[] classList = classString.split(";");
			
			for(String className : classList) {
				Class cn = Class.forName(className);
				morphia.map(cn);
			}
			
			db = morphia.createDatastore(m, "ToRead");
			db.ensureIndexes();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MongoConnectionManager instance() {
		return INSTANCE;
	}
	
	public Datastore getDb() {
		return db;
	}
}
