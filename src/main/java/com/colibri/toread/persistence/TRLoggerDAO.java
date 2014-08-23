package com.colibri.toread.persistence;

import com.google.code.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;
import com.colibri.toread.entities.TRLogger;

public class TRLoggerDAO extends BasicDAO<TRLogger, ObjectId> {
	public TRLoggerDAO() {
		super(TRLogger.class, MongoConnectionManager.instance().getDb());
	}
}
