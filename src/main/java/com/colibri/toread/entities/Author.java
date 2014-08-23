package com.colibri.toread.entities;

import org.json.JSONException;
import org.json.JSONObject;
import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;

import java.util.Date;

public class Author extends ToReadBaseEntity implements Jsonifiable {
	private String name;
	private Date birthDate;
	
	public String geName() {
		return name;
	}
	public void setName(String firstName) {
		this.name = firstName;
	}

	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("Name", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	
}
