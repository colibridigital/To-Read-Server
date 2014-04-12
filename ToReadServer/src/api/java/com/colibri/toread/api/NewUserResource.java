package com.colibri.toread.api;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.MongoResource;
import com.colibri.toread.persistence.MorphiaResource;
import com.colibri.toread.persistence.UserDAO;
import com.mongodb.WriteConcern;

public class NewUserResource extends ServerResource {

	@Get
	public String represent() {
		return "New User";
	}
	
	@Post
	public Representation acceptItem(Representation entity) throws JSONException, IOException {
		System.out.println("User attempting to login");
		
		//Get me the JSON
		JSONObject json = new JsonRepresentation(entity).getJsonObject();
					
		User user = new User(json);

		//Check we at least have a username and password, otherwise return an error string
		if(user.getUserName() == null || user.getPassword() == null) {
			return new JsonRepresentation(
					getErrorRepresentation("Invalid username or password"));
		}
		
		//Persist this new user
		UserDAO dao = new UserDAO(MorphiaResource.INSTANCE.getMorphia(), MongoResource.INSTANCE.getMongoClient());
		dao.save(user, WriteConcern.ACKNOWLEDGED);
		
		return null;
	}
	
	private JSONObject getErrorRepresentation(String message){
		JSONObject object = new JSONObject();
		try {
			object.put("login_success", false);
			object.put("auth_token", "");
			object.put("device_id", "");
			object.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return object;
}
}
