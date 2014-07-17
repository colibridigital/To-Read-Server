package com.colibri.toread.userdata;

import com.colibri.toread.ToReadBaseEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BookCollection extends ToReadBaseEntity {
	private HashMap<ObjectId, BookStatus> books = new HashMap<ObjectId, BookStatus>();
	
	public BookCollection() {
	}
	
	public ArrayList<ObjectId> getAllBooks() {
		ArrayList<ObjectId> allBooks = new ArrayList<ObjectId>();
		
		for(Iterator<Map.Entry<ObjectId, BookStatus>> it = books.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<ObjectId, BookStatus> entry = it.next();
			allBooks.add(entry.getKey());
		}
		
		return allBooks;
	}
	
	public boolean contains(ObjectId book) {
		return books.containsKey(book);
	}
	
	public void remove(ObjectId book) {
		books.remove(book);
	}
	
	public void put(ObjectId book, BookStatus status) {
		books.put(book, status);
	}
}
