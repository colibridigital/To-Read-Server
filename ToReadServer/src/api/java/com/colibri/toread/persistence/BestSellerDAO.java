package com.colibri.toread.persistence;

import org.bson.types.ObjectId;

import com.colibri.toread.auth.User;
import com.colibri.toread.entities.BestSellers;
import com.colibri.toread.entities.Book;
import com.google.code.morphia.dao.BasicDAO;

public class BestSellerDAO extends BasicDAO<BestSellers, ObjectId> {
	public BestSellerDAO() {
		super(BestSellers.class, MongoConnectionManager.instance().getDb());
	}
	
	public BestSellers findByListName(String listName){
		return ds.createQuery(BestSellers.class).field("listName").equal(listName).get();	
	}
}
