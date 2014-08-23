package com.colibri.toread.entities;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;
import com.colibri.toread.external.GoogleCoverURLResolver;

import java.util.ArrayList;
import java.util.Date;


public class BestSellers extends ToReadBaseEntity implements Jsonifiable {
	private ArrayList<BestSeller> bestSellers = new ArrayList<BestSeller>();
	private Date lastUpdated = new Date();
    private final GoogleCoverURLResolver googleResolver = new GoogleCoverURLResolver();
	
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
				
				//BestSeller object which we will populate with data
				BestSeller thisBestSeller = new BestSeller();
			    String ISBN = getBestSellerDetail(thisResult, "primary_isbn13");
				thisBestSeller.setTitle(getBestSellerDetail(thisResult, "title"));
				thisBestSeller.setCoverURL(getCoverURL(getBestSellerDetail(thisResult, "book_image"), ISBN));
				thisBestSeller.setISBN(ISBN);
				Author thisAuthor = new Author();
				thisAuthor.setName(getBestSellerDetail(thisResult, "author"));
				thisBestSeller.addAuthor(thisAuthor);
				
				bestSellers.add(thisBestSeller);
			}
			
		} catch (JSONException e) {
			logger.error("Unable to parse JSON, failed with " + e.toString());
		}
		
		//Reinitialize to today
		lastUpdated = new Date();
		logger.info("Best sellers list updated with " + bestSellers.size() + " new books");
	}

    private String getCoverURL(String bestSellResult, String ISBN) {
        if(bestSellResult.equals("null")) {
            logger.info("No cover URL returned by NYT, will try Google");
            return googleResolver.getCoverURL(ISBN);
        }

        return bestSellResult;
    }

	private String getBestSellerDetail(JSONObject result, String key) throws JSONException {
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
			for(BestSeller book : bestSellers) {
				bookArray.put(book.toJson());
			}
			
			object.put("best_sellers", bookArray);
		} catch (JSONException e) {
			logger.error(e.toString());
		}
		
		return object;
	}		
}
