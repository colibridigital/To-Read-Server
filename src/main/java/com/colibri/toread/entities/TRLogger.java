package com.colibri.toread.entities;

import org.json.JSONException;
import com.colibri.toread.ToReadBaseEntity;

import java.io.IOException;

public class TRLogger extends ToReadBaseEntity {
	private String log;
	
	public TRLogger(String data) throws JSONException, IOException {
		this.log = data;
	}
}
