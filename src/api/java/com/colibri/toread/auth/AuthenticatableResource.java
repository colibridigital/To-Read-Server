package com.colibri.toread.auth;

import com.colibri.toread.api.LoggableResource;
import com.colibri.toread.persistence.UserDAO;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;

public class AuthenticatableResource extends LoggableResource{
	Logger logger = Logger.getLogger(AuthenticatableResource.class);
	
	public boolean authenticateRequest(JSONObject json){
		String username = "";
		String deviceId = "";
		String token = "";
		
		try {
			JSONObject authData;
			
			if(json.has("auth_data"))
				authData = json.getJSONObject("auth_data");
			else
				return false;
			
			if(authData.has("username") && authData.has("device_id") && authData.has("token")) {
				username = authData.getString("username");
				deviceId = authData.getString("device_id");
				token = authData.getString("token");
			}
			else
				return false;
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		
		logger.info("Authenticating " + username + " with device " + deviceId);
		
		UserDAO dao = new UserDAO();
		User user = dao.findByUsername(username);
		
		if(user == null)
			return false;
		
		Device device = user.findDevice(deviceId);
		
		if(device == null)
			return false;
		
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
