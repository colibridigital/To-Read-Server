package com.colibri.toread.entities;

import com.colibri.toread.ToReadBaseEntity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

public class WrittenEntity extends ToReadBaseEntity {
	@Indexed (value=IndexDirection.ASC, name="bookNameIndex", unique=false)
	private String title;
	@Indexed (value=IndexDirection.ASC, name="bookISBNIndex", unique=true)
	private String ISBN;

	
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

}
