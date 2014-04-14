package com.colibri.toread.auth;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import com.colibri.toread.persistence.UserDAO;

public class AuthenticatableResource extends ServerResource{
	Logger logger = Logger.getLogger(AuthenticatableResource.class);
	
	public boolean authenticateRequest(Representation entity){
		String username = "";
		String deviceId = "";
		String token = "";
		JSONObject json;
		
		try {
			json = new JsonRepresentation(entity).getJsonObject();
			JSONObject authData;
			
			if(json.has("auth_data"))
				authData = json.getJSONObject("auth_data");
			else
				return false;
			
			if(authData.has("username") && authData.has("device_id") &&json.has("token")) {
				username = authData.getString("username");
				deviceId = authData.getString("device_id");
				token = json.getString("token");
			}
			else
				return false;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		logger.info("Authenticating " + username + " with device " + deviceId);
		
		UserDAO dao = new UserDAO();
		User user = dao.findByUsername(username);
		Device device = user.findDevice(deviceId);
		
		return device.validateToken(token);
	}
	
	public JsonRepresentation authenticationError(){
		JSONObject object = new JSONObject();
		try {
			object.put("auth_result", false);
			object.put("message", "Invalid auth token");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		logger.warn("Device authentication failed");		
		return new JsonRepresentation(object);
	}
}
