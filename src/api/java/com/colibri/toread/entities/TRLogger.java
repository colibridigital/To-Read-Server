package com.colibri.toread.entities;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import com.colibri.toread.ToReadBaseEntity;

public class TRLogger extends ToReadBaseEntity{
	private String log;
	
	public TRLogger(String data) throws JSONException, IOException {
		this.log = data;
	}
}
