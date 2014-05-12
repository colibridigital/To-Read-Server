package com.colibri.toread.api;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.colibri.toread.auth.Device;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;
import com.google.code.morphia.Key;

public class InitDeviceResource extends LoggableResource {
	
	private static Logger logger = Logger.getLogger(InitDeviceResource.class);

	@Get
	public String represent() {
		return "Initialisation of a new device";
	}
	
	@Post
	public Representation acceptItem(Representation entity) throws JSONException, IOException {		
		logger.info("Creating new user with random credentials");
		
		//Get me the JSON
		JSONObject json = new JsonRepresentation(entity).getJsonObject();
		JSONObject deviceJSON;
		
		//Log it
		logRequest(json.toString());
		
		//Get the device specific JSON
		if(json.has("device") )
			deviceJSON = json.getJSONObject("device");
		else
			return getResponseRepresentation(false, "No data for a device was supplied");
		
		User user = new User();
		
		//Generates a mongo format Id which should be unique
		user.generateRandomUsername();
		Device device = new Device();
		Key<User> saveResult;
				
		if(!device.deviceFromJSON(deviceJSON))
			return getResponseRepresentation(false, "Some field was missing from the device JSON");
		
		//Add the device to the user object
		user.addDevice(device);
		
		//Persist this new user with the device embedded
		UserDAO dao = new UserDAO();
		if(dao.findByUsername(user.getUserName()) == null )
			saveResult = dao.save(user);
		else
			return new JsonRepresentation(getResponseRepresentation(false, "Username already exists"));

		//Did the database accept the write operation?
		if(saveResult.getId() == null)
			return getResponseRepresentation(false, "A server error occured please try again");
		
		//Now update the username to be the object id generated by the database, we know this is unique
		user.setUserName(user.getObjectId().toString());
		dao.save(user);
		
		logger.info("Username assigned to new user was " + user.getUserName());
		
		//If all is good return a JSON representation of the user and the device we created
		if(saveResult.getId() != null)
			return new JsonRepresentation(user.toJson());
		else
			return getResponseRepresentation(false, "A server error occured please try again");
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
