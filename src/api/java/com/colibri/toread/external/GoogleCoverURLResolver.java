package com.colibri.toread.external;

import com.colibri.toread.ReadProperties;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class GoogleCoverURLResolver {
	private static Logger logger = Logger.getLogger(GoogleCoverURLResolver.class);

	//Get Cover URL
	public String getCoverURL(String ISBN) {
		JSONObject json = new JSONObject();
        String thumbnail = "";
		
		try {
			json = readJsonFromUrl(buildURL(ISBN));

            if(json.getInt("totalItems") == 0) {
                logger.warn("Nothing was found for ISBN " + ISBN + " nowhere else to search, returning blank cover URL");
                return thumbnail;
            }

            JSONArray items = json.getJSONArray("items");

            //Invalid ISBN or other
            if(items.length() == 0) {
                logger.warn("No data returned for lookup on ISBN" + ISBN);
                return "";
            }

            JSONObject thisItem = (JSONObject) items.get(0);
            JSONObject volumeInfo = thisItem.getJSONObject("volumeInfo");
            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            thumbnail = imageLinks.getString("thumbnail");
        } catch(Exception e) {
			logger.error(e.toString());
		}

        logger.info("Returning image url: " + thumbnail);
		return thumbnail;
	}

	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	//http://api.nytimes.com/svc/books/v2/lists/2014-06-01/Combined-Print-and-E-Book-Fiction.json?&offset=&sortby=&sortorder=&api-key=####
	private String buildURL(String ISBN) {
		String baseURL = ReadProperties.getProperty("google_base_url");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseURL);
		stringBuilder.append(ISBN);

		logger.info("Request URL for covers is " + stringBuilder.toString());

		return stringBuilder.toString();
	}
}
