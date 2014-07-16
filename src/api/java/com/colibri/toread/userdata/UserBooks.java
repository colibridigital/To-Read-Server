package com.colibri.toread.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.colibri.toread.ToReadBaseEntity;

public class UserBooks extends ToReadBaseEntity{
	
	//Hashmap of collecton name to book collection. The book collection class has another hashmap
	//of Book (ObjectId) to Book status
	private HashMap<String, BookCollection> collections = new HashMap<String, BookCollection>();
	private HashSet<ObjectId> deletedBooks = new HashSet<ObjectId>();
	private static Logger logger = Logger.getLogger(UserBooks.class);
	
	public boolean addCollection(String collectionName) {
		
		//Check to see if this already exists, if it does dont put it in again
		if(collections.containsKey(collectionName))
			return false;
		
		collections.put(collectionName, new BookCollection());
		logger.info("Collection " + collectionName + " was added");
		return true;
	}
	
	public boolean removeCollection(String collectionName) {
		if(!collections.containsKey(collectionName)) {
			logger.warn("Attempting to remove a collection " + collectionName + " which doesnt exist");
			return false;
		}
		
		//Get all the books for the collection
		BookCollection thisCollection = collections.get(collectionName);
		ArrayList<ObjectId> books = thisCollection.getAllBooks();
		
		//Add all books in the collection to the deleted books set
		for(ObjectId book : books) {
			//Add to the deleted books set
			if(!deletedBooks.contains(book))
				deletedBooks.add(book);
		}
		
		collections.remove(collectionName);
		logger.info("Collection " + collectionName + " was removed");
		return true;
	}
	
	public boolean markAsDeleted(String collectionName, ObjectId book) {
		//Check the collection and book exist to remove
		if(!collections.containsKey(collectionName)) {
			logger.warn("Attempting to delete a book from collection " + collectionName + " which doesnt exist");
			return false;
		}
		
		BookCollection thisCollection = collections.get(collectionName);
		
		if(!thisCollection.contains(book)) {
			logger.warn("Book not in collection " + collectionName + " so can't delete");
			return false;
		}
				
		thisCollection.remove(book);
		collections.put(collectionName, thisCollection);
		
		if(!deletedBooks.contains(book))
			deletedBooks.add(book);
		
		logger.info("Book " + book + " was deleted from collection " + collectionName);
		return true;
	}

	public boolean addBook(String collectionName, ObjectId book) {
		if(!collections.containsKey(collectionName)) {
			logger.warn("Attempting to add a book to collection " + collectionName + " which doesnt exist, will create");
			collections.put(collectionName, new BookCollection());
		}
			
		BookCollection thisCollection = collections.get(collectionName);			
		
		//Book already exists
		if(thisCollection.contains(book) ) {
			logger.warn("User already has book " + book + " in collection " + collectionName + " so won't add it again");
			return false;
		}
		
		thisCollection.put(book, new BookStatus());
		collections.put(collectionName, thisCollection);
		logger.info("Book " + book + " was added to collection " + collectionName);
		
		if(deletedBooks.contains(book)) {
			logger.info("Book also found in deleted books, will remove from that list");
			deletedBooks.remove(book);
		}
		return true;
	}

	public boolean hasBook(String collectionName, ObjectId book) {
		if(!collections.containsKey(collectionName)) {
			logger.warn("Attempting to search for a book in collection " + collectionName + " which doesnt exist");
			return false;
		}
		
		BookCollection thisCollection = collections.get(collectionName);	
		return thisCollection.contains(book);
	}
	
	public void removeClientsDeletedBooks(String collectionName, ArrayList<ObjectId> clientsBooks) {
		if(collections.containsKey(collectionName)) {
			
			BookCollection  thisCollection = collections.get(collectionName);			
			ArrayList<ObjectId> books = thisCollection.getAllBooks();
			
			//Loop over my books for the collection, if the client sent no info for the book then we mark it as deleted
			for(ObjectId book : books ) {
				
				//Book is deleted
				if(!clientsBooks.contains(book)) {
					markAsDeleted(collectionName, book); 
				}
			}
		}
		else
			logger.warn("Attempting to remove deleted books in collection " + collectionName + " which doesnt exist");
	}
}
