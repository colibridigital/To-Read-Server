package com.colibri.toread.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.colibri.toread.ReadProperties;

//Connects to the times best seller API and downloads the best seller list for today
public class TimesBestSeller {
	private static Logger logger = Logger.getLogger(TimesBestSeller.class);

	//Get JSON for today
	public JSONObject loadData() {
		logger.info("Attempting to load data from NY Times best sellers API");
		JSONObject json = new JSONObject();
		
		try {
			json = readJsonFromUrl(buildURL());
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		return json;
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
	private String buildURL() {
		String baseURL = ReadProperties.getProperty("base_url");
		String APIKey = ReadProperties.getProperty("api_key");
		String bookList = ReadProperties.getProperty("book_list");
		//The .json&offset garbage
		String formatUrl = ReadProperties.getProperty("format_url");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseURL);
		stringBuilder.append("/");
		stringBuilder.append(getTodaysDate());
		stringBuilder.append("/");
		stringBuilder.append(bookList);
		stringBuilder.append(formatUrl);
		stringBuilder.append(APIKey);

		logger.info("Request URL is " + stringBuilder.toString());

		return stringBuilder.toString();
	}

	private String getTodaysDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		// Output "2012-09-26"
		String formatted = format1.format(cal.getTime());
		logger.info("Formatted date for today is " + formatted);
		return formatted;	
	}
}
