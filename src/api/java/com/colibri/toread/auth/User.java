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
    //1: 0 - 25, 2: 25 - 40, 3: 40 - 65, 4: 65+;
    private int ageRange;
    private String occupation;
    private String sex;
	
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
				logger.info("First name field not found in user info JSON");
			}

            if(json.has("age_range") ) {
                this.ageRange = json.getInt("age_range");
            }
            else {
                logger.info("Age range field not found in user info JSON");
            }

            if(json.has("sex") ) {
                this.sex = json.getString("sex");
            }
            else {
                logger.info("Sex field not found in user info JSON");
            }

            if(json.has("occupation") ) {
                this.occupation = json.getString("occupation");
            }
            else {
                logger.info("Occupation field not found in user info JSON");
            }
			
			if(json.has("last_name") ) {
				this.setLastName(json.getString("last_name"));
			}
			else {
				logger.info("Last name field not found in user info JSON");
			}
			
			if(json.has("email_address") ) {
				this.setEmailAddress(json.getString("email_address"));
			}
			else {
				logger.info("Email address field not found in user info JSON");
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

            if(ageRange != 0)
                json.put("age_range", ageRange);

            if(sex != null)
                json.put("sex", sex);

            if(occupation != null)
                json.put("occupation", occupation);
			
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
	
	public void markAsDeleted(String collectionName, String ISBN) {
		logger.info("Deleting " + ISBN + " from collection " + collectionName);
		
		if(!myBooks.markAsDeleted(collectionName, ISBN))
			logger.info("User didn't have the book in the first place, so won't do anything");
		
		logger.info("Book Id " + ISBN + " was marked for deletion");
	}
	
	public void addBook(String collectionName, String newBook) {
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
	
	public void removeDeletedBooks(HashMap<String, ArrayList<String>> clientsBooks) {
		//Loop over each collection one by one
		for(Iterator<Map.Entry<String, ArrayList<String>>> it = clientsBooks.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, ArrayList<String>> entry = it.next();

            String collectionName = entry.getKey();
            myBooks.removeClientsDeletedBooks(collectionName, entry.getValue());
		}

        myBooks.removeDeletedCollections(clientsBooks.keySet());
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
	
	public boolean hasBook(String collectionName, String ISBN) {
		return myBooks.hasBook(collectionName, ISBN);
	}

	public String getUserName() {
		return userName;
	}

    public JSONObject getBooksJSON() {
        return myBooks.toJson();
    }
}
	
