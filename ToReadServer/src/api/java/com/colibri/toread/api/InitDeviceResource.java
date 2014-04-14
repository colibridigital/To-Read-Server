package com.colibri.toread.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.colibri.toread.auth.Device;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;

public class InitDeviceResource extends ServerResource {

	@Get
	public String represent() {
		return "hello, world";
	}
	
	@Post
	public Representation acceptItem(Representation entity) {		
		//The first time a device connects to the service without logging in
		//ie a new device installation
		
		System.out.println("New device connecting");
		Form form = new Form(entity);
		
		User newUser = new User();
		UserDAO dao = new UserDAO();
		
		//Initialise the device
		Device thisDevice = new Device();
		thisDevice.setPlatform(form.getFirstValue("platform"));
		
		//Add the device to the user and save 
		newUser.addDevice(thisDevice);
		dao.save(newUser);
		
		JSONObject deviceJSON = thisDevice.toJson();
		try {
			deviceJSON.append("user_id", newUser.getObjectId().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new JsonRepresentation(deviceJSON);
	}

}
