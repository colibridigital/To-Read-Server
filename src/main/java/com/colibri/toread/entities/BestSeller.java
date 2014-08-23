package com.colibri.toread.entities;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.colibri.toread.Jsonifiable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James on 22/07/2014.
 */
@Data
public class BestSeller extends WrittenEntity implements Jsonifiable {
    private ArrayList<Author> authors = new ArrayList<Author>();
    private int edition;
    private String publisher;
    private Date publishDate;
    private String coverURL;

    public BestSeller() {}

    public BestSeller(JSONObject json) {
        try {
            this.setTitle(json.getString("title"));
            this.setISBN(json.getString("ISBN"));
            edition = json.getInt("edition");
            coverURL = json.getString("cover_url");

            //Get authors
            if(json.has("authors")) {
                JSONArray authors = json.getJSONArray("authors");
                for(int i = 0; i < authors.length(); i++) {
                    JSONObject thisAuthor = authors.getJSONObject(i);

                    Author newAuthor = new Author();
                    newAuthor.setName(thisAuthor.getString("name"));

                    this.authors.add(newAuthor);
                }
            }

            if(json.has("publish_date")) {
                ////E.g "January 2, 2010"
                Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(json.getString("publish_date"));
                publishDate = date;
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
            json.put("cover_url", coverURL);
            JSONArray authorsArr = new JSONArray();

            for(Author author : authors) {
                authorsArr.put(author.toJson());
            }

            json.put("authors", authorsArr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public void addAuthor(Author author ) {
        authors.add(author);
    }

}
