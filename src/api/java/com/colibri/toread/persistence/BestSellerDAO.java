package com.colibri.toread.persistence;

import com.colibri.toread.entities.BestSellers;
import com.google.code.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

public class BestSellerDAO extends BasicDAO<BestSellers, ObjectId> {
	public BestSellerDAO() {
		super(BestSellers.class, MongoConnectionManager.instance().getDb());
	}
	
	public BestSellers findByListName(String listName){
		return ds.createQuery(BestSellers.class).field("listName").equal(listName).get();	
	}
}
