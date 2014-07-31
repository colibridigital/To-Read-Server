package com.colibri.toread.api;

import com.colibri.toread.auth.Device;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;
import com.google.code.morphia.Key;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.IOException;

public class NewUserResource extends LoggableResource {
	
	private static Logger logger = Logger.getLogger(NewUserResource.class);

	@Get
	public String represent() {
		return "New User";
	}
	
	@Post
	public Representation acceptItem(Representation entity) throws JSONException, IOException {
		logger.info("Attempting to create new user");
		
		//Get me the JSON
		JSONObject json = new JsonRepresentation(entity).getJsonObject();
		JSONObject userJSON;
		JSONObject deviceJSON;
		
		//Log it
		logRequest(json.toString());
		
		//Get the user specific JSON
		if(json.has("user") )
			userJSON = json.getJSONObject("user");
		else
			return getResponseRepresentation(false, null, "No data for a user was supplied");
		
		//Get the device specific JSON
		if(json.has("device") )
			deviceJSON = json.getJSONObject("device");
		else
			return getResponseRepresentation(false, null, "No data for a device was supplied");
		
		User user = new User();
		Device device = new Device();
		Key<User> saveResult;
		
		//Parse the JSON and set fields on the object
		if(!user.userFromJSON(userJSON))
			return getResponseRepresentation(false, null, "Some field was missing from the user JSON");
		
		logger.info("Username is " + user.getUserName());
		
		if(!device.deviceFromJSON(deviceJSON))
			return getResponseRepresentation(false, null, "Some field was missing from the device JSON");

		//Add the device to the user object
		user.addDevice(device);
		
		//Persist this new user with the device embedded
		UserDAO dao = new UserDAO();
		if(dao.findByUsername(user.getUserName()) == null )
			saveResult = dao.save(user);
		else
			return new JsonRepresentation(getResponseRepresentation(false, null, "Username already exists"));

		//Did the database accept the write operation?
		if(saveResult.getId() != null)
			return getResponseRepresentation(true, device, "New user created");
		else
			return getResponseRepresentation(false, null, "A server error occured please try again");
	}
	
	private JsonRepresentation getResponseRepresentation(boolean success, Device device, String message){
		JSONObject object = new JSONObject();
		try {
			object.put("operation_success", success);
			if(device != null)
				object.put("device", device.toJson());
			object.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		logger.info("Response to client was " + " sucess: " + success + " message: " + message);		
		return new JsonRepresentation(object);
	}

}
