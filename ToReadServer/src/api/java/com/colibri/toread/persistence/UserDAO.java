package com.colibri.toread.persistence;

import org.bson.types.ObjectId;

import com.colibri.toread.auth.User;
import com.colibri.toread.entities.Book;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.MongoClient;

public class UserDAO extends BasicDAO<User, ObjectId> {
	public UserDAO( Morphia morphia, MongoClient mongo) {
		super(mongo, morphia, "userCollection");
	}
}
