package com.colibri.toread.userdata;

import com.colibri.toread.ToReadBaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BookCollection extends ToReadBaseEntity {
	private HashMap<String, BookStatus> books = new HashMap<String, BookStatus>();
	
	public BookCollection() {
	}
	
	public ArrayList<String> getAllBooks() {
		ArrayList<String> allBooks = new ArrayList<>();
		
		for(Iterator<Map.Entry<String, BookStatus>> it = books.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, BookStatus> entry = it.next();
			allBooks.add(entry.getKey());
		}
		
		return allBooks;
	}
	
	public boolean contains(String ISBN) {
		return books.containsKey(ISBN);
	}
	
	public void remove(String ISBN) {
		books.remove(ISBN);
	}
	
	public void put(String ISBN, BookStatus status) {
		books.put(ISBN, status);
	}
}
