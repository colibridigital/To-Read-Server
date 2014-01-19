package com.colibri.toread.api;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.colibri.toread.auth.AuthenticatableResource;
import com.colibri.toread.entities.Book;
import com.colibri.toread.persistence.BookDAO;
import com.colibri.toread.persistence.MongoResource;
import com.colibri.toread.persistence.MorphiaResource;
import com.colibri.toread.persistence.UserDAO;

public class GetBooksResource extends AuthenticatableResource{

	@Post
	public Representation acceptItem(Representation entity) {	
		if(!authenticateRequest(entity))
				return getErrorResponse();
		
		Form form = new Form(entity);
		String userId = form.getFirstValue("user_id");
		
		ArrayList<ObjectId> bookIds = new UserDAO(MorphiaResource.INSTANCE.getMorphia(), MongoResource.INSTANCE.getMongoClient()).getBookIdsForUser(userId);
		
		BookDAO bookDAO = new BookDAO(MorphiaResource.INSTANCE.getMorphia(), MongoResource.INSTANCE.getMongoClient());
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		//Populate the in memory book list
		for(ObjectId id : bookIds){
			bookList.add(bookDAO.get(id));
		}
		return entity;
	}
}
