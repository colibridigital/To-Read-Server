package com.colibri.toread.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;

import com.colibri.toread.ToReadBaseEntity;
import com.google.code.morphia.annotations.Indexed;

public class User extends ToReadBaseEntity {
	@Indexed private String user_name;
	private String email_address;
	private Password password;
	private ArrayList<Device> devices = new ArrayList<Device>(); //All of a users registered devices
	private String first_name;
	private String last_name;
	private Date dob;
	//Index list of book ids so we can retrieve the books as user has without holding
	//direct copies of them
	@Indexed
	private ArrayList<ObjectId> book_list = new ArrayList<ObjectId>();

	public void setNewPassword(String password){
		try {
			this.password.encrypt(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validatePassword(String password){
		if(password == null)
			return false;
		
		try {
			return this.password.validate(password);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	//Add a new device to this user object
	public void addDevice(Device newDevice){
		devices.add(newDevice);
	}
	
	public Device findDevice(String id){
		for(Device device : devices){
			if(device.getObjectId().toString().compareTo(id) == 0)
				return device;
		}

		return null;
	}
	
	public void setUserName(String user_name){
		this.user_name = user_name;
	}
	
	public ArrayList<ObjectId> getBookIds(){
		return this.book_list;
	}
}
	
