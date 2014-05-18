package com.colibri.toread.userdata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.bson.types.ObjectId;

public class UserBooks {
	//Hashmap of users book collections.  Each key is the name of the collection, the value is another hashmap
	//containing the collection books.  That hashamp is a map of the books ObjectId to book status
	private HashMap<String, HashMap<ObjectId, BookStatus>> collections = new HashMap<String, HashMap<ObjectId, BookStatus>>();
	private HashSet<ObjectId> deletedBooks = new HashSet<ObjectId>();
	
	public boolean addCollection(String collectionName) {
		
		//Check to see if this already exists, if it does dont put it in again
		if(collections.containsKey(collectionName))
			return false;
		
		collections.put(collectionName, new HashMap<ObjectId, BookStatus>());
		return true;
	}
	
	public boolean removeCollection(String collectionName) {
		if(!collections.containsKey(collectionName))
			return false;
		
		HashMap<ObjectId, BookStatus> thisCollection = collections.get(collectionName);
			
		//Add all books in the collection to the deleted books set
		for(Iterator<Map.Entry<ObjectId, BookStatus>> it = thisCollection.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<ObjectId, BookStatus> entry = it.next();
			
			//Add to the deleted books set
			if(!deletedBooks.contains(entry.getKey()))
				deletedBooks.add(entry.getKey());
		}
		
		collections.remove(collectionName);
		return true;
	}
}
