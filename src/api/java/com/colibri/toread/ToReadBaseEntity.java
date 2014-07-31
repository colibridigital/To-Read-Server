package com.colibri.toread;

import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;

public abstract class ToReadBaseEntity {
	@Id private ObjectId id;
	
	public ObjectId getObjectId(){
		return id;
	}
	
	public void setObjectId(ObjectId newId){
		this.id = newId;
	}
}
