package com.colibri.toread.entities;

import com.colibri.toread.ToReadBaseEntity;
import org.json.JSONException;

import java.io.IOException;

public class TRLogger extends ToReadBaseEntity{
	private String log;
	
	public TRLogger(String data) throws JSONException, IOException {
		this.log = data;
	}
}
