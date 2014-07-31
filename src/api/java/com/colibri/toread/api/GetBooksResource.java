package com.colibri.toread.api;

import com.colibri.toread.auth.AuthenticatableResource;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public class GetBooksResource extends AuthenticatableResource{

	@Post
	public Representation acceptItem(Representation entity) {	
	
		return entity;
	}
}
