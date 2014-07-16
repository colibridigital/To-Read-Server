package com.colibri.toread.entities;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

public class BestSellers extends ToReadBaseEntity implements Jsonifiable{
	private ArrayList<Book> bestSellers = new ArrayList<Book>();
	private Date lastUpdated = new Date();
	
	@Indexed(value=IndexDirection.ASC, name="sellerListIndex", unique=true)
	private String listName;
	
	public BestSellers(){
		
	}
	
	public BestSellers(String listName) {
		this.listName = listName;
	}
	
	private static Logger logger = Logger.getLogger(BestSellers.class);
	
	public boolean requiresUpdate() {
		if(DateUtils.isSameDay(lastUpdated, new Date()) && (bestSellers.size() != 0)) {
			logger.info("Best seller list updated today, nothing to do");
			return false;
		}
		
		logger.info("Seller list last updated on " + lastUpdated.toString() + " will update again");
		return true;
	}
	
	public void updateFromJson(JSONObject json) {
		//Empty out the current book list as we will replace it with this
		bestSellers.clear();
		
		try {
			JSONArray resultJSON = json.getJSONArray("results");
			
			for(int i= 0; i < resultJSON.length(); i++) {
				JSONObject thisResult = resultJSON.getJSONObject(i);
				
				//Book object which we will populate with data
				Book thisBook = new Book();
			
				thisBook.setTitle(getBookDetail(thisResult, "title"));
				thisBook.setCoverURL(getBookDetail(thisResult, "book_image"));
				thisBook.setISBN(getBookDetail(thisResult, "primary_isbn13"));
				Author thisAuthor = new Author();
				thisAuthor.setName(getBookDetail(thisResult, "author"));
				thisBook.addAuthor(thisAuthor);
				
				bestSellers.add(thisBook);
			}
			
		} catch (JSONException e) {
			logger.error("Unable to parse JSON, failed with " + e.toString());
		}
		
		//Reinitialize to today
		lastUpdated = new Date();
		logger.info("Best sellers list updated with " + bestSellers.size() + " new books");
	}
	
	private String getBookDetail(JSONObject result, String key) throws JSONException {
		JSONArray detailsArr = result.getJSONArray("book_details");
		//Get the first one
		JSONObject details = detailsArr.getJSONObject(0);
		return details.getString(key);
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("lastUpdated", lastUpdated.toString());
			
			JSONArray bookArray = new JSONArray();
			for(Book book : bestSellers) {
				bookArray.put(book.toJson());
			}
			
			object.put("best_sellers", bookArray);
		} catch (JSONException e) {
			logger.error(e.toString());
		}
		
		return object;
	}		
}
