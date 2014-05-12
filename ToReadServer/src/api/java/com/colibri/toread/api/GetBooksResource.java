package com.colibri.toread.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.colibri.toread.auth.AuthenticatableResource;

public class GetBooksResource extends AuthenticatableResource{

	@Post
	public Representation acceptItem(Representation entity) {	
	
		return entity;
	}
}
