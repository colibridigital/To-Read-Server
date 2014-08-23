package com.colibri.toread;

import org.apache.log4j.Logger;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.Series;
import com.colibri.toread.loadio.LoadIOApplication;
import com.colibri.toread.web.WebApplication;

public class Bootstrap {
	static Logger logger = Logger.getLogger(Bootstrap.class);
		public static void main(String[] args) throws Exception {

			//Create a new Component.
			Component component = new Component();
			
			//Allow file handling
			component.getClients().add(Protocol.FILE);
			
			//Create a new HTTPS server listening on port 2709.
			Server server = component.getServers().add(Protocol.HTTP, 2709);
			Series<Parameter> params = server.getContext().getParameters(); 
			
			//Add the SSL certficiate
//			params.add("sslContextFactory", "org.restlet.ext.ssl.PkixSslContextFactory");
//			params.add("keystorePath", "server.jks");
//			params.add("keystorePassword", "password");
//			params.add("keyPassword", "password");
//			params.add("keystoreType", "JKS");
			
//			BookDAO dao = new BookDAO();
//						Book book = new Book();
//						Author author = new Author();
//						author.setFirstName("James");
//						author.setLastName("Cross");
//						book.addAuthor(author);
//						book.setTitle("There and back again");
//					book.setPublisher("Tolkein books");
//						
//						dao.save(book, WriteConcern.ACKNOWLEDGED);
//						System.out.println(("done"));

            component.getContext().getParameters().add("maxThreads", "1024");
            component.getContext().getParameters().add("minThreads", "128");
            component.getContext().getParameters().add("lowThreads", "900");
            component.getContext().getParameters().add("maxQueued", "-1");

			component.getDefaultHost().attach("/web", new WebApplication());
			component.getDefaultHost().attach("/api", new ToReadApplication());
			component.getDefaultHost().attach("/loaderio-83bd0a466cb318e0b32148de82750aa7", new LoadIOApplication());

			//Start the component.
			component.start();
		}
}
