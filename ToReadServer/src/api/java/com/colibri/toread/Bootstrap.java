package com.colibri.toread;

import com.colibri.toread.entities.Author;
import com.colibri.toread.entities.Book;
import com.colibri.toread.persistence.BookDAO;
import com.colibri.toread.persistence.MongoResource;
import com.colibri.toread.persistence.MorphiaResource;
import com.colibri.toread.web.*;
import com.mongodb.WriteConcern;

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
			
			//Create a new HTTPS server listening on port 2709.
			Server server = component.getServers().add(Protocol.HTTP, 2709);
			Series<Parameter> params = server.getContext().getParameters(); 
			
			//Add the SSL certficiate
//			params.add("sslContextFactory", "org.restlet.ext.ssl.PkixSslContextFactory");
//			params.add("keystorePath", "server.jks");
//			params.add("keystorePassword", "password");
//			params.add("keyPassword", "password");
//			params.add("keystoreType", "JKS");
			
			BookDAO dao = new BookDAO(MorphiaResource.INSTANCE.getMorphia(), MongoResource.INSTANCE.getMongoClient());
						Book book = new Book();
						Author author = new Author();
						author.setFirstName("James");
						author.setLastName("Cross");
						book.addAuthor(author);
						book.setTitle("There and back again");
					book.setPublisher("Tolkein books");
						
						dao.save(book, WriteConcern.ACKNOWLEDGED);
						System.out.println(("done"));

			component.getDefaultHost().attach("/web", new WebApplication());
			component.getDefaultHost().attach("/api", new ToReadApplication());
			
			//Start the component.
			component.start();
		}
}
