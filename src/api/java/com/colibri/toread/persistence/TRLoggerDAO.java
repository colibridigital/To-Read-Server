package com.colibri.toread.persistence;

import com.colibri.toread.entities.TRLogger;
import com.google.code.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

public class TRLoggerDAO extends BasicDAO<TRLogger, ObjectId> {
	public TRLoggerDAO() {
		super(TRLogger.class, MongoConnectionManager.instance().getDb());
	}
}
