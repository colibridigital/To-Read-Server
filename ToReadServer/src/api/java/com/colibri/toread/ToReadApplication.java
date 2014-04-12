package com.colibri.toread;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

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
		router.attach("/register", NewUserResource.class);
				
		return router;
	}

}