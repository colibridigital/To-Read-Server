package com.colibri.toread.api;

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

public class AdMobConfigResource extends AuthenticatableResource {
	
	@Get
	public String represent() {
		return "5";
	}
}
