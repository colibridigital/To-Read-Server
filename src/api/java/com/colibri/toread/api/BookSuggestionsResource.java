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

public class BookSuggestionsResource extends AuthenticatableResource {
	
	private static Logger logger = Logger.getLogger(BookSuggestionsResource.class);

	@Get
	public String represent() {
		return "Get book suggestions";
	}
	
	@Post
	public Representation acceptItem(Representation entity) throws JSONException, IOException {		
		logger.info("Getting book suggestions");

		JSONObject json = new JsonRepresentation(entity).getJsonObject();
		
		//Log it
		logRequest(json.toString());
		
		if(!authenticateRequest(json))
			return authenticationError();

		BestSellerDAO dao = new BestSellerDAO();
		BestSellers bestSellers = dao.findByListName("Combined Print and E-Book Fiction");
		
		if(bestSellers == null) {
			bestSellers = new BestSellers("Combined Print and E-Book Fiction");
		}
		
		//Load the latest data from the new york times if required
		if(bestSellers.requiresUpdate()) {
			TimesBestSeller bs = new TimesBestSeller();
			bestSellers.updateFromJson(bs.loadData());
		}
		
		//Update the db
		dao.save(bestSellers);
		
		return new JsonRepresentation(bestSellers.toJson());
	}
	
	private JsonRepresentation getResponseRepresentation(boolean success, String message){
		JSONObject object = new JSONObject();
		try {
			object.put("operation_success", success);
			object.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		logger.info("Response to client was " + " sucess: " + success + " message: " + message);		
		return new JsonRepresentation(object);
	}

}
