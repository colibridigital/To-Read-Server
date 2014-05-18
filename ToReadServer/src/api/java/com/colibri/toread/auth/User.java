package com.colibri.toread.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;
import com.colibri.toread.userdata.UserBooks;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity
public class User extends ToReadBaseEntity implements Jsonifiable{
	@Indexed(value=IndexDirection.ASC, name="userNameIndex", unique=true)
	private String userName;
	private String emailAddress;
	private Password password;
	@Embedded
	private ArrayList<Device> devices = new ArrayList<Device>(); //All of a users registered devices
	private String firstName;
	private String lastName;
	private Date dob;
	
	//Index list of book ids so we can retrieve the books as user has without holding
	//direct copies of them
	//Map of object ID to boolean flag indicating if the book has been read or not
	private HashMap<ObjectId, Boolean> bookMap = new HashMap<ObjectId, Boolean>();
	private HashSet<ObjectId> deletedBooks = new HashSet<ObjectId>();
	
	private UserBooks myBooks;
	
	private static Logger logger = Logger.getLogger(User.class);
	
	public User() {	
	}
	
	public boolean userFromJSON(JSONObject json) {
		try {
			if(json.has("username") ) {
				this.setUserName(json.getString("username"));
			}
			else {
				logger.error("Username field not found in user info JSON");
				return false;
			}
			
			if(json.has("first_name") ) {
				this.setFirstName(json.getString("first_name"));
			}
			else {
				logger.error("First name field not found in user info JSON");
				return false;
			}
			
			if(json.has("last_name") ) {
				this.setLastName(json.getString("last_name"));
			}
			else {
				logger.error("Last name field not found in user info JSON");
				return false;
			}
			
			if(json.has("email_address") ) {
				this.setEmailAddress(json.getString("email_address"));
			}
			else {
				logger.error("Email address field not found in user info JSON");
				return false;
			}
			
			if(json.has("password") ) {
				this.setNewPassword(json.getString("password"));
			}
			else {
				logger.error("Password field not found in user info JSON");
				return false;
			}
						
			if(json.has("dob") ) {
				String dateString = json.getString("dob");
				//E.g "January 2, 2010"
				Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateString);
				this.setDob(date);
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		return true;
	}
	
	//Use a mongo format object Id as the starting point
	public void generateRandomUsername() {
		this.userName = ObjectId.get().toString();
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		try {
			json.put("user_name", userName);
			
			if(emailAddress != null) 
				json.put("email_addres", emailAddress);
			
			if(firstName != null)
				json.put("first_name", firstName);
			
			if(lastName != null)
				json.put("last_name", lastName);
			
			if(dob != null)
				json.put("dob", dob);
			
			JSONArray jsonArray = new JSONArray();
			for(Device device : devices) {
				jsonArray.put(device.toJson());
			}
			
			json.put("devices", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
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
	
	public void markAsDeleted(ObjectId bookId) {
		//Remove a book from the active map and move to the deleted set
		deletedBooks.add(bookId);
		logger.info("Book Id " + bookId.toString() + " was marked for deletion");
	}
	
	public void addBook(ObjectId newBook) {
		if(!bookMap.containsKey(newBook)) {
			bookMap.put(newBook, false);
			logger.info("Added book to the user");
		}
		else
			logger.info("User already has this book, skipping");
		if(deletedBooks.contains(newBook)) {
			deletedBooks.remove(newBook);
			logger.info("User has this book marked as deleted, removing from that list");
		}
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
	
	public void processClientBooks(HashSet<ObjectId> clientsBooks) {
		for(Iterator<Map.Entry<ObjectId, Boolean>> it = bookMap.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<ObjectId, Boolean> entry = it.next();

			//Book is deleted
			if(!clientsBooks.contains(entry.getKey())) {
				markAsDeleted(entry.getKey()); 
				it.remove();
			}
		}
	}
	
	public void setUserName(String userName){
		this.userName = userName;
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
	
	public boolean hasBook(ObjectId bookId) {
		return bookMap.containsKey(bookId);
	}

	public String getUserName() {
		return userName;
	}
	
}
	
