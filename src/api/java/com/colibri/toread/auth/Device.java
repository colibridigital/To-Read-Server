package com.colibri.toread.auth;

import com.colibri.toread.Jsonifiable;
import com.colibri.toread.ToReadBaseEntity;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

public class Device extends ToReadBaseEntity implements Jsonifiable{
	private String deviceOSId;
	private String deviceMake;
	private String deviceModel;
	private String platform;
	private String osVersion;
	private String cellularNetwork;
	private Token authToken;
	
	private static Logger logger = Logger.getLogger(Device.class);
	
	public Device(){
		this.authToken = new Token();
		this.authToken.generateToken();
		this.setObjectId(ObjectId.get());
	}
	
	public boolean deviceFromJSON(JSONObject json) {
		try {
			if(json.has("deviceOSId") ) {
				this.setDeviceOSId(json.getString("deviceOSId"));
			}
			else {
				logger.error("Device OS Id field not found in user info JSON");
				return false;
			}
			
			if(json.has("device_make") ) {
				this.setDeviceMake(json.getString("device_make"));
			}
			else {
				logger.error("Device make field not found in user info JSON");
				return false;
			}
			
			if(json.has("device_model") ) {
				this.setDeviceModel(json.getString("device_model"));
			}
			else {
				logger.error("Device model field not found in user info JSON");
				return false;
			}
			
			if(json.has("platform") ) {
				this.setPlatform(json.getString("platform"));
			}
			else {
				logger.error("Platform field not found in user info JSON");
				return false;
			}
			
			//Non essential fields
			if(json.has("os_version") ) {
				this.setOsVersion(json.getString("osVersion"));
			}
			
			if(json.has("cell_network") ) {
				this.setCellularNetwork(json.getString("cell_network"));
			}
					
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	

	
	public String getDeviceOSId() {
		return deviceOSId;
	}

	public void setDeviceOSId(String deviceOSId) {
		this.deviceOSId = deviceOSId;
	}

	public String getDeviceMake() {
		return deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getCellularNetwork() {
		return cellularNetwork;
	}

	public void setCellularNetwork(String cellularNetwork) {
		this.cellularNetwork = cellularNetwork;
	}

	public void setAuthToken(Token authToken) {
		this.authToken = authToken;
	}

	public boolean validateToken(String token){
		if(authToken == null)
			return false;
		
		return this.authToken.validateToken(token);
	}
	
	public String getToken() {
		return authToken.getToken();
	}
	
	public JSONObject toJson(){
		JSONObject object = new JSONObject();
		
		try {
			object.put("tr_device_id", getObjectId().toString());
			object.put("auth_token", authToken.getToken());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return object;
	}
}
