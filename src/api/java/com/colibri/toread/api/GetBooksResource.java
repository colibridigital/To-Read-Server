package com.colibri.toread.api;

import com.colibri.toread.auth.AuthenticatableResource;
import com.colibri.toread.auth.User;
import com.colibri.toread.persistence.UserDAO;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import java.io.IOException;

public class GetBooksResource extends AuthenticatableResource{

    private static Logger logger = Logger.getLogger(GetBooksResource.class);

	@Post
	public Representation acceptItem(Representation entity) throws IOException, JSONException {
        logger.info("Client requesting all books");

        JSONObject json = new JsonRepresentation(entity).getJsonObject();

        if(!authenticateRequest(json))
            return authenticationError();

        //Log it
        logRequest(json.toString());
        String username = json.getJSONObject("auth_data").getString("username");

        //Load the user from the database
        User user = new User();
        UserDAO userDAO = new UserDAO();
        user = userDAO.findByUsername(username);

		return new JsonRepresentation(user.getBooksJSON());
	}
}
