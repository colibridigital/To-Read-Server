package com.colibri.toread.api;

import com.colibri.toread.auth.AuthenticatableResource;
import org.restlet.resource.Get;

public class IAdConfigResource extends AuthenticatableResource {
	
	@Get
	public String represent() {
		return "5";
	}
}
