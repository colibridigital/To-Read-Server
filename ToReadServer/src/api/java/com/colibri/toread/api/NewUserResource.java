package com.colibri.toread.api;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.colibri.toread.auth.User;

public class NewUserResource extends ServerResource {

	@Get
	public String represent() {
		return "hello, world";
	}
	
	@Post
	public Representation acceptItem(Representation entity) {
		System.out.println("User attempting to login");
		Form form = new Form(entity);
		
		String username = form.getFirstValue("user_name");
		String password = form.getFirstValue("password");
		
		User newUser = new User();
		newUser.setUserName(username);
		
		return null;
	}
}
