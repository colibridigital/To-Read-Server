package com.colibri.toread.persistence;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.colibri.toread.entities.Book;
import com.google.code.morphia.dao.BasicDAO;

public class BookDAO extends BasicDAO<Book, ObjectId> {
	public BookDAO() {
		super(Book.class, MongoConnectionManager.instance().getDb());
	}
	
	public ArrayList<Book> getBooksForUser(String user_id){
		ArrayList<Book> bookList = new ArrayList<Book>();
			
		return bookList;
	}
}
