package com.colibri.toread.api;

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

import java.io.IOException;

public class UserDeviceUpdateResource extends AuthenticatableResource {
	
	private static Logger logger = Logger.getLogger(UserDeviceUpdateResource.class);

	@Get
	public String represent() {
		return "Updating user and or device details";
	}
	
	@Post
	public Representation acceptItem(Representation entity) throws JSONException, IOException {		
		logger.info("Updating user");

		JSONObject json = new JsonRepresentation(entity).getJsonObject();
		
		//Log it
		logRequest(json.toString());
		
		if(!authenticateRequest(json))
			return getResponseRepresentation(false, "username or password invalid");
	
		JSONObject deviceJSON;
		JSONObject userJSON;
		
		//Get the user specific JSON
		if(json.has("user"))
			userJSON = json.getJSONObject("user");
		else
			return getResponseRepresentation(false, "No data for a user was supplied");
		
		//Get the device specific JSON
		if(json.has("device") )
			deviceJSON = json.getJSONObject("device");
		else
			return getResponseRepresentation(false, "No data for a device was supplied");
		
		//Get the device specific JSON
		if(!userJSON.has("username") )
			return getResponseRepresentation(false, "No username supplied");
		
		UserDAO userDAO = new UserDAO();
		
		//Load the user from the database
		User user = userDAO.findByUsername(userJSON.getString("username"));
		
		logger.info("Updating user and device info for " + user.getUserName());
		
		//Update the user and device data based on the client supplied JSON
		user = processUserUpdates(user, userJSON, deviceJSON);
		
		//Save it back to the database
		userDAO.save(user);
		
		return getResponseRepresentation(true, "User information updated");
	}
	
	private User processUserUpdates(User user, JSONObject userJSON, JSONObject deviceJSON) throws JSONException {
		//Update user data
		if(userJSON.has("username"))
			user.setUserName(userJSON.getString("username"));
		
		if(userJSON.has("email_address"))
			user.setEmailAddress(userJSON.getString("email_address"));
		
		if(userJSON.has("password"))
			user.setNewPassword(userJSON.getString("password"));
		
		if(userJSON.has("first_name"))
			user.setFirstName(userJSON.getString("first_name"));
		
		if(userJSON.has("last_name"))
			user.setLastName(userJSON.getString("last_name"));
		
		//Now update the device
		//Device device = user.findDevice(deviceJSON.getString("device_id"));
		//Update device information here if required
		
		return user;
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
