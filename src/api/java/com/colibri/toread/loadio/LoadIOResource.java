package com.colibri.toread.loadio;

import com.colibri.toread.auth.AuthenticatableResource;
import com.colibri.toread.entities.BestSellers;
import com.colibri.toread.external.TimesBestSeller;
import com.colibri.toread.persistence.BestSellerDAO;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.IOException;

public class LoadIOResource extends AuthenticatableResource {
	
	private static Logger logger = Logger.getLogger(LoadIOResource.class);

	@Get
	public String represent() {
		return "loaderio-83bd0a466cb318e0b32148de82750aa7";
	}

}
