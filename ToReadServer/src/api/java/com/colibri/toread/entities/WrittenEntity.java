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
}
