package com.colibri.toread.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Book extends WrittenEntity{
	public Book() {
	}
	
	public Book(JSONObject json) {
		try {
			this.setTitle(json.getString("title"));
			this.setEdition(json.getInt("edition"));
			this.setCoverURL(json.getString("cover_url"));
			
			////E.g "January 2, 2010"
			Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(json.getString("publish_date"));
			this.setPublishDate(date);
			
			//Get authors
			JSONArray authors = json.getJSONArray("authors");
			for(int i = 0; i < authors.length(); i++) {
				JSONObject thisAuthor = authors.getJSONObject(i);
				
				Author newAuthor = new Author();
				newAuthor.setFirstName(thisAuthor.getString("first_name"));
				newAuthor.setLastName(thisAuthor.getString("last_name"));

				this.addAuthor(newAuthor);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
