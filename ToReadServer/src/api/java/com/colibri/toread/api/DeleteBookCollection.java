package com.colibri.toread.api;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.colibri.toread.auth.AuthenticatableResource;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;

public class DeleteBookCollection extends AuthenticatableResource {
	
	private static Logger logger = Logger.getLogger(DeleteBookCollection.class);

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
		
		if(!json.has("collection"))
			return getResponseRepresentation(false, "Collection was missing");
		
		boolean success = user.removeBookCollection(json.getString("collection"));
		userDAO.save(user);
		
		return getResponseRepresentation(success, "");
	}
	
	private JsonRepresentation getResponseRepresentation(boolean success, String message){
		JSONObject object = new JSONObject();
		try {
			object.put("operation_success", success);
			object.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		logger.info("Response to client was " + " success: " + success + " message: " + message);		
		return new JsonRepresentation(object);
	}

}
