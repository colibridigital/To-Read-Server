package com.colibri.toread.entities;

import java.util.ArrayList;
import java.util.Date;

import com.colibri.toread.ToReadBaseEntity;

public class WrittenEntity extends ToReadBaseEntity {
	private String title;
	private ArrayList<Author> authors = new ArrayList<Author>();
	private int edition;
	private String publisher;
	private Date publish_date;
	private String cover_photo_url;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}
	public int getEdition() {
		return edition;
	}
	public void setEdition(int edition) {
		this.edition = edition;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Date getPublish_date() {
		return publish_date;
	}
	public void setPublish_date(Date publish_date) {
		this.publish_date = publish_date;
	}
	public String getCover_photo_url() {
		return cover_photo_url;
	}
	public void setCover_photo_url(String cover_photo_url) {
		this.cover_photo_url = cover_photo_url;
	}
	
	public void addAuthor(Author newAuthor) {
		authors.add(newAuthor);
	}
}
