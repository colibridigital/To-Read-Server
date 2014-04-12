package com.colibri.toread.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.colibri.toread.ToReadBaseEntity;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

@Entity
public class User extends ToReadBaseEntity {
	private String userName;
	private String emailAddress;
	private Password password;
	private ArrayList<Device> devices = new ArrayList<Device>(); //All of a users registered devices
	private String firstName;
	private String lastName;
	private Date dob;
	//Index list of book ids so we can retrieve the books as user has without holding
	//direct copies of them
	private ArrayList<ObjectId> book_list = new ArrayList<ObjectId>();
	
	public User() {	
	}
	
	public User(JSONObject json) {
		try {
			this.setUserName(json.getString("username"));
			this.setEmailAddress(json.getString("email_address"));
			this.setFirstName(json.getString("first_name"));
			this.setLastName(json.getString("last_name"));
			
			if(json.has("publish_date") ) {
				String dateString = json.getString("publish_date");
				//E.g "January 2, 2010"
				Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateString);
				this.setDob(date);
			}
			
			//Encrypt the password
			this.setNewPassword(json.getString("password"));		
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setNewPassword(String password){
		try {
			this.password = new Password();
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
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public ArrayList<ObjectId> getBookIds(){
		return this.book_list;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Password getPassword() {
		return password;
	}

	public ArrayList<Device> getDevices() {
		return devices;
	}

	public void setDevices(ArrayList<Device> devices) {
		this.devices = devices;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public ArrayList<ObjectId> getBook_list() {
		return book_list;
	}

	public void setBook_list(ArrayList<ObjectId> book_list) {
		this.book_list = book_list;
	}

	public String getUserName() {
		return userName;
	}
	
}
	
