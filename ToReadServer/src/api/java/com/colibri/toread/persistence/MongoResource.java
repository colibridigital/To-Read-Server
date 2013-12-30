package com.colibri.toread.persistence;

import java.net.UnknownHostException;

import com.colibri.toread.ReadProperties;
import com.mongodb.MongoClient;

public enum MongoResource {
	INSTANCE;
	private MongoClient mongoClient;
	
	private MongoResource() {
		if(mongoClient == null)
			try {
				mongoClient = initClient();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	}
	
	private MongoClient initClient() throws UnknownHostException {
		int port = Integer.parseInt(ReadProperties.getProperty("mongoport"));
		String host = ReadProperties.getProperty("dbserver");
		return new MongoClient(host, port);
	}
	
	public MongoClient getMongoClient() {
		return mongoClient;
	}
}
