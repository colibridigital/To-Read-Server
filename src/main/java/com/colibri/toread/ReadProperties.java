package com.colibri.toread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
	private static Properties trProps = null;
		
	//Load static properties from the properties file
	private static void load() {
		try {
			File propsFile = new File("ToRead.properties");
			FileInputStream fileInput = new FileInputStream(propsFile);
			
			trProps = new Properties();
			trProps.load(fileInput);
			
			fileInput.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Go get the property we want
	public static String getProperty(String property) {
		if(trProps == null)
			load();
				
		return(trProps.getProperty(property));
	}

}
