package com.colibri.toread.auth;

import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;
import com.colibri.toread.userdata.UserBooks;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	
	@Embedded
	private UserBooks myBooks = new UserBooks();
	
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
	
	public void markAsDeleted(String collectionName, ObjectId bookId) {
		logger.info("Deleting " + bookId + " from collection " + collectionName);
		
		if(!myBooks.markAsDeleted(collectionName, bookId))
			logger.info("User didn't have the book in the first place, so won't do anything");
		
		logger.info("Book Id " + bookId.toString() + " was marked for deletion");
	}
	
	public void addBook(String collectionName, ObjectId newBook) {
		myBooks.addBook(collectionName, newBook);
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
	
	public void processClientBooks(HashMap<String, ArrayList<ObjectId>> clientsBooks) {
		//Loop over each collection one by one
		for(Iterator<Map.Entry<String, ArrayList<ObjectId>>> it = clientsBooks.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, ArrayList<ObjectId>> entry = it.next();
			
			myBooks.removeClientsDeletedBooks(entry.getKey(), entry.getValue());
		}
	}
	
	public boolean removeBookCollection(String collectionName) {
		return myBooks.removeCollection(collectionName);
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
	
	public boolean hasBook(String collectionName, ObjectId bookId) {
		return myBooks.hasBook(collectionName, bookId);
	}

	public String getUserName() {
		return userName;
	}
	
}
	
