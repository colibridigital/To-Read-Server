package com.colibri.toread.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.colibri.toread.auth.AuthenticatableResource;
import com.colibri.toread.auth.User;
import com.colibri.toread.entities.Book;
import com.colibri.toread.persistence.BookDAO;
import com.colibri.toread.persistence.UserDAO;

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

		ArrayList<String> newBooks = new ArrayList<String>();
		HashSet<ObjectId> clientsBooks = new HashSet<ObjectId>();
		
		//Current book object
		JSONObject thisJSONBook;
		
		//Check each book in the client list to see if the server knows about it.  If it does, then that is common on both
		//sides
		//If the client has it and the server does not, then we will respond with that sub list to the client
		//If the client doesn't have it and the server does, then we mark it as deleted on the server side
		for(int i = 0; i < clientBookList.length(); i++) {		
			try {
				thisJSONBook = clientBookList.getJSONObject(i);
				
				//We have an ISBN but no object ID, so it's a new book
				if(thisJSONBook.has("ISBN") && !thisJSONBook.has("id") ) {
					
					String ISBN = thisJSONBook.getString("ISBN");
					//Just make sure they arent sending us ISBNs again
					if(!userHasISBN(ISBN, user))
						newBooks.add(ISBN);
					else {
						//We need to load this book and add it to the in memory store of client books
						//So when we loop over the server books it's there and we know the client didnt delete it
						logger.info("User already has this book so the ISBN " + ISBN + " is erroneous");
						clientsBooks.add(loadBookFromISBN(ISBN).getObjectId());
					}
											
					continue;
				}
				
				//The client already has an object ID for the book, hence we must have issued it, and the server knows about it
				if(thisJSONBook.has("id") && !thisJSONBook.has("ISBN")) {			
					//Add this the hashset of clients books we know about already
					ObjectId thisObjectId = new ObjectId(thisJSONBook.getString("id"));
					clientsBooks.add(thisObjectId);
					continue;
				}					

			} catch (JSONException e) {
				e.printStackTrace();
				return new JSONObject();
			}
		}
			
		//Iterate over the servers books and check if the client has sent information for it.  Remove books they didnt send
		//information for
		user.processClientBooks(clientsBooks);
		
		//Cross reference the books we just found with those in the database, for those we already have a record of we don't need to request
		//the info from the client.  Books in the database will be added to the user as a reference
		newBooks = loadBooksFromDb(user, newBooks);
		logger.info("Found " + newBooks.size() + " new books to request data for");
		
		//Persist the updated user object to the database
		UserDAO userDAO = new UserDAO();
		userDAO.save(user);
		
		return bookListToJSON(newBooks);
	}
	
	private boolean userHasISBN(String ISBN, User user) {
		BookDAO bookDAO = new BookDAO();
		if(bookDAO.exists("ISBN", ISBN)) {
			Book thisBook = bookDAO.findByISBN(ISBN);
			return user.hasBook(thisBook.getObjectId());
		}
		
		return false;
	}
	
	private Book loadBookFromISBN(String ISBN) {
		BookDAO bookDAO = new BookDAO();
		return bookDAO.findByISBN(ISBN);
	}
	
	private ArrayList<String> loadBooksFromDb(User user, ArrayList<String> newBooks) {
		BookDAO bookDAO = new BookDAO();
		
		//Loop over the books and check to see if they exist in the database, if they do then just add them directly
		Iterator<String> it = newBooks.iterator();
		while(it.hasNext()) {
			String thisISBN = it.next();
			
			if(bookDAO.exists("ISBN", thisISBN)) {
				it.remove();	
				//Find the object ID for the book with this ISBN
				Book thisBook = bookDAO.findByISBN(thisISBN);
				user.addBook(thisBook.getObjectId());
				logger.info("Added book with ISBN " + thisISBN + " to user, sent by client and already exists in the database");
			}
		}
		
		return newBooks;
	}
	
	private JSONObject bookListToJSON(ArrayList<String> bookList) {
		JSONObject listObject = new JSONObject();
		JSONArray array = new JSONArray(bookList);
		
		try {
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
