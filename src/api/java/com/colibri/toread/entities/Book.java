package com.colibri.toread.entities;

import com.colibri.toread.Jsonifiable;
import org.json.JSONException;
import org.json.JSONObject;

public class Book extends WrittenEntity implements Jsonifiable{
	public Book() {
	}

	public Book(JSONObject json) {
		try {
			this.setTitle(json.getString("title"));
			this.setISBN(json.getString("ISBN"));

		} catch (JSONException e) {
            e.printStackTrace();
        }
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		try {
			json.put("title", getTitle());
			json.put("ISBN", getISBN());
			json.put("id", getObjectId());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}
}
