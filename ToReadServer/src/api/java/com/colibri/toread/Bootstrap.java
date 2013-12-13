package com.colibri.toread;

import com.colibri.toread.web.*;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.Series;

public class Bootstrap {
		public static void main(String[] args) throws Exception {

			//Create a new Component.
			Component component = new Component();
			
			//Allow file handling
			component.getClients().add(Protocol.FILE);
			
			//Create a new HTTPS server listening on port 2620.
			Server server = component.getServers().add(Protocol.HTTP, 2709);
			Series<Parameter> params = server.getContext().getParameters(); 

			component.getDefaultHost().attach("/web", new WebApplication());
			component.getDefaultHost().attach("/api", new ToReadApplication());
			
			//Start the component.
			component.start();
		}
}
