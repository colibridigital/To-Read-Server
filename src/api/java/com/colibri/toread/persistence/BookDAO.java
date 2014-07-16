package com.colibri.toread.persistence;

import org.bson.types.ObjectId;

import com.colibri.toread.entities.Book;
import com.google.code.morphia.dao.BasicDAO;

public class BookDAO extends BasicDAO<Book, ObjectId> {
	public BookDAO() {
		super(Book.class, MongoConnectionManager.instance().getDb());
	}
	
	public Book findByISBN(String ISBN){
		return ds.createQuery(Book.class).field("ISBN").equal(ISBN).get();	
	}
}
