package com.colibri.toread.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class InitDeviceResource extends ServerResource {

	@Get
	public String represent() {
		return "hello, world";
	}
	
	@Post
	public Representation acceptItem(Representation entity) {		
		return null;
	}

}
