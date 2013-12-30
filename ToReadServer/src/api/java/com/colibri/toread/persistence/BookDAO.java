package com.colibri.toread.persistence;

import org.bson.types.ObjectId;

import com.colibri.toread.entities.Book;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.MongoClient;

public class BookDAO extends BasicDAO<Book, ObjectId> {
	public BookDAO( Morphia morphia, MongoClient mongo) {
		super(mongo, morphia, "bookCollection");
	}
}