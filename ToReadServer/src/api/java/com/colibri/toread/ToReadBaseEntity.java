package com.colibri.toread;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;

public abstract class ToReadBaseEntity {
	@Id private ObjectId id;
	
	public ObjectId getObjectId(){
		return id;
	}
	
	public void setObjectId(ObjectId newId){
		this.id = newId;
	}
}
