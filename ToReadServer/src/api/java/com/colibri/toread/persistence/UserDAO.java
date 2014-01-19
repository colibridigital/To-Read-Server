package com.colibri.toread.persistence;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.colibri.toread.auth.User;
import com.colibri.toread.entities.Book;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.MongoClient;

public class UserDAO extends BasicDAO<User, ObjectId> {
	public UserDAO( Morphia morphia, MongoClient mongo) {
		super(mongo, morphia, "userCollection");
	}
	
	public User findByUsername(String username){
		return ds.createQuery(User.class).field("user_name").equal(username).get();	
	}
	
	//TODO: This wont really work so fix it
	//probably want an index of device ids to user ids so given a device id can
	//quickly get a user id
	public User findByDeviceId(String device_id){
		return ds.createQuery(User.class).field("device_id").equal(device_id).get();
	}
	
	public ArrayList<ObjectId> getBookIdsForUser(String user_id){
		ObjectId id = new ObjectId(user_id);
		User thisUser = this.get(id);
		return thisUser.getBookIds();
	}	
}
