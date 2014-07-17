package com.colibri.toread.persistence;

import com.colibri.toread.auth.User;
import com.google.code.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

public class UserDAO extends BasicDAO<User, ObjectId> {
	public UserDAO() {
		super(User.class, MongoConnectionManager.instance().getDb());
	}
	
	public User findByUsername(String username){
		return ds.createQuery(User.class).field("userName").equal(username).get();	
	}
	
	//TODO: This wont really work so fix it
	//probably want an index of device ids to user ids so given a device id can
	//quickly get a user id
	public User findByDeviceId(String device_id){
		return ds.createQuery(User.class).field("device_id").equal(device_id).get();
	}	
}
