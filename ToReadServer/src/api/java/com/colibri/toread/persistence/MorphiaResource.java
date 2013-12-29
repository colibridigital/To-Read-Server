package com.colibri.toread.persistence;

import java.net.UnknownHostException;

import com.colibri.toread.ReadProperties;
import com.google.code.morphia.Morphia;

public enum MorphiaResource {
	INSTANCE;
	private Morphia morphia;
	
	private MorphiaResource() {
		if(morphia == null)
			try {
				morphia = initMorphia();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	//Create a static morphia instance and map to the class names we will be using
	private Morphia initMorphia() throws ClassNotFoundException {
		morphia = new Morphia();
		
		String classString = ReadProperties.getProperty("classmap");
		String[] classList = classString.split(";");
		
		for(String className : classList) {
			Class cn = Class.forName(className);
			morphia.map(cn);
		}	
		
		return(morphia);
	}
	
	public Morphia getMorphia() {
		return(morphia);
	}
}
