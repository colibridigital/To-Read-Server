package com.colibri.toread;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.colibri.toread.api.ClientListResource;
import com.colibri.toread.api.InitDeviceResource;
import com.colibri.toread.api.LoginResource;
import com.colibri.toread.api.NewUserResource;

public class ToReadApplication extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attach("/initdevice", InitDeviceResource.class);
		router.attach("/login", LoginResource.class);
		
		//Register new things
		router.attach("/register/user", NewUserResource.class);
		
		//Book sync API
		//Take a client list of books and cross reference them against the database.  Delete books not present and return the elements
		//we know nothing about
		router.attach("/sync/clientlist", ClientListResource.class);
				
		return router;
	}

}