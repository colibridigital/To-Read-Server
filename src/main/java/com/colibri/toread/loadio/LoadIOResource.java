package com.colibri.toread.loadio;

import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import com.colibri.toread.auth.AuthenticatableResource;

public class LoadIOResource extends AuthenticatableResource {
	
	private static Logger logger = Logger.getLogger(LoadIOResource.class);

	@Get
	public String represent() {
		return "loaderio-83bd0a466cb318e0b32148de82750aa7";
	}

}
