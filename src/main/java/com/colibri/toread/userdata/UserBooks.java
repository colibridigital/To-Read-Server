package com.colibri.toread.userdata;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;

import java.util.*;

public class UserBooks extends ToReadBaseEntity implements Jsonifiable {

    //Hashmap of collecton name to book collection. The book collection class has another hashmap
    //of Book (ObjectId) to Book status
    private HashMap<String, BookCollection> collections = new HashMap<String, BookCollection>();
    private HashSet<String> deletedBooks = new HashSet<String>();
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
        ArrayList<String> books = thisCollection.getAllBooks();

        //Add all books in the collection to the deleted books set
        for(String ISBN : books) {
            //Add to the deleted books set
            if(!deletedBooks.contains(ISBN))
                deletedBooks.add(ISBN);
        }

        collections.remove(collectionName);
        logger.info("Collection " + collectionName + " was removed");
        return true;
    }

    public boolean hasCollection(String collectionName) {
        return collections.containsKey(collectionName);
    }

    public boolean markAsDeleted(String collectionName, String ISBN) {
        //Check the collection and book exist to remove
        if(!collections.containsKey(collectionName)) {
            logger.warn("Attempting to delete a book from collection " + collectionName + " which doesnt exist");
            return false;
        }

        BookCollection thisCollection = collections.get(collectionName);

        if(!thisCollection.contains(ISBN)) {
            logger.warn("Book not in collection " + collectionName + " so can't delete");
            return false;
        }

        thisCollection.remove(ISBN);
        collections.put(collectionName, thisCollection);

        if(!deletedBooks.contains(ISBN))
            deletedBooks.add(ISBN);

        logger.info("Book " + ISBN + " was deleted from collection " + collectionName);
        return true;
    }

    public boolean addBook(String collectionName, String ISBN) {
        if(!collections.containsKey(collectionName)) {
            logger.warn("Attempting to add a book to collection " + collectionName + " which doesnt exist, will create");
            addCollection(collectionName);
        }

        BookCollection thisCollection = collections.get(collectionName);

        //Book already exists
        if(thisCollection.contains(ISBN) ) {
            logger.warn("User already has book " + ISBN + " in collection " + collectionName + " so won't add it again");
            return false;
        }

        thisCollection.put(ISBN, new BookStatus());
        collections.put(collectionName, thisCollection);
        logger.info("Book " + ISBN + " was added to collection " + collectionName);

        if(deletedBooks.contains(ISBN)) {
            logger.info("Book also found in deleted books, will remove from that list");
            deletedBooks.remove(ISBN);
        }
        return true;
    }

    public boolean hasBook(String collectionName, String ISBN) {
        if(!collections.containsKey(collectionName)) {
            logger.warn("Attempting to search for a book in collection " + collectionName + " which doesnt exist");
            return false;
        }

        BookCollection thisCollection = collections.get(collectionName);
        return thisCollection.contains(ISBN);
    }

    public void removeClientsDeletedBooks(String collectionName, ArrayList<String> clientsBooks) {
        if(collections.containsKey(collectionName)) {

            BookCollection thisCollection = collections.get(collectionName);
            ArrayList<String> books = thisCollection.getAllBooks();

            //Loop over my books for the collection, if the client sent no info for the book then we mark it as deleted
            for(String ISBN : books ) {

                //Book is deleted
                if(!clientsBooks.contains(ISBN)) {
                    markAsDeleted(collectionName, ISBN);
                }
            }
        }
        else
            logger.warn("Attempting to remove deleted books in collection " + collectionName + " which doesnt exist");
    }

    public void removeDeletedCollections(Set<String> clientCollections) {
        //Loop over server collections to see if the client still has them

        for(Iterator<Map.Entry<String, BookCollection>> it = collections.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String,BookCollection> entry = it.next();

            String collectionName = entry.getKey();
            if(!clientCollections.contains(collectionName)) {
                logger.info("Removing deleted collection " + entry.getKey());
                it.remove();
            }

        }
    }

    @Override
    public JSONObject toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray collections = new JSONArray();

            for(Iterator<Map.Entry<String, BookCollection>> it = this.collections.entrySet().iterator(); it.hasNext(); ) {
                JSONObject collection = new JSONObject();
                JSONArray books = new JSONArray();
                Map.Entry<String, BookCollection> entry = it.next();
                String collectionName = entry.getKey();
                ArrayList<String> allBooks = entry.getValue().getAllBooks();

                for (String book : allBooks) {
                    books.put(book);
                }

                collection.put("collection_name", collectionName);
                collection.put("books", books);

                collections.put(collection);
            }

            jsonObject.put("collections", collections);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
