package com.colibri.toread.auth;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import com.colibri.toread.persistence.MongoResource;
import com.colibri.toread.persistence.MorphiaResource;
import com.colibri.toread.persistence.UserDAO;

public class AuthenticatableResource extends ServerResource{
	public boolean authenticateRequest(Representation entity){
		//Find the user for this device and load them, also loading the device
		//We will need to load the user later anyway so we may as well cache it up front
		//Then grab this exact device from memory and authenticate the supplied auth token
		//Against the one we have in the database for it
		Form form = new Form(entity);
		
		String device_id = form.getFirstValue("tr_device_id");
		String auth_token = form.getFirstValue("auth_token");
		
		UserDAO dao = new UserDAO(MorphiaResource.INSTANCE.getMorphia(), MongoResource.INSTANCE.getMongoClient());
		User user = dao.findByDeviceId(device_id);
		Device thisDevice = user.findDevice(device_id);
		
		return thisDevice.validateToken(auth_token);
	}
	
	public Representation getErrorResponse(){
		JSONObject object = new JSONObject();
		try {
			object.append("authenticated", false);
			object.append("message", "Device authentication failed");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return(new JsonRepresentation(object));
	}
}
