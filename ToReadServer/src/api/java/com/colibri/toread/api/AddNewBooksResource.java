package com.colibri.toread.api;

import java.io.IOException;
import java.util.ArrayList;

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
import com.colibri.toread.entities.Book;
import com.colibri.toread.persistence.BookDAO;
import com.colibri.toread.persistence.UserDAO;

public class AddNewBooksResource extends AuthenticatableResource {
	
	private static Logger logger = Logger.getLogger(AddNewBooksResource.class);

	@Get
	public String represent() {
		return "Add new books";
	}
	

	@Post
	public Representation acceptItem(Representation entity) throws JSONException, IOException {
		logger.info("Recieving new book(s) to be added form the client");
		
		JSONObject json = new JsonRepresentation(entity).getJsonObject();
		
		if(!authenticateRequest(json))
			return authenticationError();
		
		//Log it
		logRequest(json.toString());
		String username = json.getJSONObject("auth_data").getString("username");
		logger.info("Username is " + username);
		
		UserDAO userDAO = new UserDAO();
		User user = userDAO.findByUsername(username);
		
		//Get list of book ids
		JSONArray bookArray;
		if(!json.has("book_list"))
			return getResponseRepresentation(false, null, "Booklist was missing");
			
		bookArray = json.getJSONArray("book_list");
		ArrayList<Book> bookList = new ArrayList<Book>();
		for(int i = 0; i < bookArray.length(); i++) {
			JSONObject bookObj = bookArray.getJSONObject(i);
			
			Book newBook = saveBook(bookObj.getJSONObject("Book"), user);
			bookList.add(newBook);
			user.addBook(bookObj.getString("collection"), newBook.getObjectId());
		}
		
		//Update the user object
		userDAO.save(user);
		
		return getResponseRepresentation(true, bookList, "Save completed");
	}
	
	private Book saveBook(JSONObject json, User user) {
		BookDAO bookDAO = new BookDAO();	
		Book book = new Book(json);
		
		if(!bookDAO.exists("ISBN", book.getISBN())){
			logger.info("Saving book with title " + book.getTitle());
			bookDAO.save(book);
		}
		else {
			logger.info("Book with title " + book.getTitle() + " already exists in the database, skipping");
			book = bookDAO.findByISBN(book.getISBN());
		}
		return book;
	}
	
	private JsonRepresentation getResponseRepresentation(boolean success, ArrayList<Book> bookList, String message){
		JSONObject object = new JSONObject();
		try {
			object.put("operation_success", success);
			object.put("message", message);
			
			if(bookList != null) {
				JSONArray bookArray = new JSONArray();
				for(Book book : bookList)
					bookArray.put(book.toJson());
				
				object.put("saved_books", bookArray);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		logger.info("Response to client was " + " sucess: " + success + " message: " + message);		
		return new JsonRepresentation(object);
	}

}
