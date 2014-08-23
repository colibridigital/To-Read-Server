package com.colibri.toread.api;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import com.colibri.toread.auth.AuthenticatableResource;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClientListResource extends AuthenticatableResource {

    private static Logger logger = Logger.getLogger(ClientListResource.class);

    @Get
    public String represent() {
        return "Client List Response";
    }

    //Take a client list of books and cross reference them against the database.  Delete books not present and return the elements
    //we know nothing about
    @Post
    public Representation acceptItem(Representation entity) throws JSONException, IOException {
        logger.info("Client sync request - step one cross referencing client books with DB");

        JSONObject json = new JsonRepresentation(entity).getJsonObject();

        if(!authenticateRequest(json))
            return authenticationError();

        //Log it
        logRequest(json.toString());
        String username = json.getJSONObject("auth_data").getString("username");

        //Load the user from the database
        User user = new User();
        UserDAO userDAO = new UserDAO();
        user = userDAO.findByUsername(username);

        //Get list of book ids
        JSONArray bookArray;
        if(!json.has("book_ids"))
            return getResponseRepresentation(false, "Booklist was missing");

        bookArray = json.getJSONArray("book_ids");

        return new JsonRepresentation(processBookList(user, bookArray));
    }

    private JSONObject processBookList(User user, JSONArray clientBookList) {
        logger.info("Begin processing of booklist for user " + user.getUserName());

        //Map of ISBN to collection name
        HashMap<String, String> newBooks = new HashMap<String, String>();

        //Map of collection name to list of ISBNs
        HashMap<String, ArrayList<String>> clientBookMap = new HashMap<String, ArrayList<String>>();

        //Current book object
        JSONObject thisJSONBook;

        //Check each book in the client list to see if the server knows about it.  If it does, then that is common on both
        //sides
        //If the client has it and the server does not, then we will add it
        //If the client doesn't have it and the server does, then we mark it as deleted on the server side
        for(int i = 0; i < clientBookList.length(); i++) {
            try {
                thisJSONBook = clientBookList.getJSONObject(i);

                if(!thisJSONBook.has("collection")) {
                    logger.error("Book supplied without a collection with index " + i + " will skip");
                    continue;
                }

                if(!thisJSONBook.has("ISBN")) {
                    logger.error("Book supplied without an ISBN with index " + i + " will skip");
                    continue;
                }

                String ISBN = thisJSONBook.getString("ISBN");
                String collection = thisJSONBook.getString("collection");

                if(!user.hasBook(collection, ISBN)) {
                    user.addBook(collection, ISBN);
                    logger.info("Added book with ISBN " + ISBN + " to collection " + collection);
                } else {
                    logger.info("Book with ISBN " + ISBN + " already exists in collection " + collection);
                }

                addBookToCollectionMap(clientBookMap, ISBN, collection);
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }

        //Iterate over the servers books and check if the client has sent information for it.  Remove books they didnt send
        //information for
        //TODO: FIX!
        user.removeDeletedBooks(clientBookMap);

        //Persist the updated user object to the database
        UserDAO userDAO = new UserDAO();
        userDAO.save(user);

        return bookListToJSON(newBooks);
    }

    private void addBookToCollectionMap(HashMap<String, ArrayList<String>> clientBookMap, String ISBN, String collection) {
        if(!clientBookMap.containsKey(collection))
            clientBookMap.put(collection, new ArrayList<String>());

        ArrayList<String> collectionBooks = clientBookMap.get(collection);
        collectionBooks.add(ISBN);
        clientBookMap.put(collection, collectionBooks);
    }

    private JSONObject bookListToJSON(HashMap<String, String> bookList) {

        JSONObject listObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for(Iterator<Map.Entry<String, String>> it = bookList.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> thisEntry = it.next();

                JSONObject bookObject = new JSONObject();
                bookObject.put("ISBN", thisEntry.getKey());
                bookObject.put("Collection", thisEntry.getValue());

                logger.info("Requesting book info for ISBN " + thisEntry.getKey() + " in collection " + thisEntry.getValue());
                array.put(bookObject);
            }

            listObject.put("book_list", array);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }

        return listObject;
    }

    private JsonRepresentation getResponseRepresentation(boolean success, String message){
        JSONObject object = new JSONObject();
        try {
            object.put("operation_success", success);
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        logger.info("Response to client was " + " sucess: " + success + " message: " + message);
        return new JsonRepresentation(object);
    }

}
