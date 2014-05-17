package com.colibri.toread.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.colibri.toread.Jsonifiable;

public class Book extends WrittenEntity implements Jsonifiable{
	public Book() {
	}
	
	
	public Book(JSONObject json) {
		try {
			this.setTitle(json.getString("title"));
			this.setISBN(json.getString("ISBN"));
			this.setEdition(json.getInt("edition"));
			this.setCoverURL(json.getString("cover_url"));
			
			//Get authors
			if(json.has("authors")) {
				JSONArray authors = json.getJSONArray("authors");
				for(int i = 0; i < authors.length(); i++) {
					JSONObject thisAuthor = authors.getJSONObject(i);
				
					Author newAuthor = new Author();
					newAuthor.setFirstName(thisAuthor.getString("first_name"));
					newAuthor.setLastName(thisAuthor.getString("last_name"));

					this.addAuthor(newAuthor);
				}
			}
			
			if(json.has("publish_date")) {
			////E.g "January 2, 2010"
				Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(json.getString("publish_date"));
				this.setPublishDate(date);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
