package com.colibri.toread.entities;

import java.util.ArrayList;
import java.util.Date;

import com.colibri.toread.ToReadBaseEntity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

public class WrittenEntity extends ToReadBaseEntity {
	@Indexed (value=IndexDirection.ASC, name="bookNameIndex", unique=false)
	private String title;
	@Indexed (value=IndexDirection.ASC, name="bookISBNIndex", unique=true)
	private String ISBN;
	private ArrayList<Author> authors = new ArrayList<Author>();
	private int edition;
	private String publisher;
	private Date publishDate;
	private String coverURL;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getISBN() {
		return ISBN;
	}
	
	public void setISBN(String newISBN) {
		this.ISBN = newISBN;
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
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publish_date) {
		this.publishDate = publish_date;
	}
	public String getcoverURL() {
		return coverURL;
	}
	public void setCoverURL(String cover_photo_url) {
		this.coverURL = cover_photo_url;
	}
	
	public void addAuthor(Author newAuthor) {
		authors.add(newAuthor);
	}
}
