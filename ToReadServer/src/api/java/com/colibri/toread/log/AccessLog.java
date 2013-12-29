package com.colibri.toread.log;

import java.util.Date;

import com.colibri.toread.auth.Device;
import com.colibri.toread.ToReadBaseEntity;
import com.colibri.toread.auth.User;

public class AccessLog extends ToReadBaseEntity {
	private Device device;
	private User user;
	private Date access_time;
	private String request_string;
	private boolean is_authorised;
	
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getAccess_time() {
		return access_time;
	}
	public void setAccess_time(Date access_time) {
		this.access_time = access_time;
	}
	public String getRequest_string() {
		return request_string;
	}
	public void setRequest_string(String request_string) {
		this.request_string = request_string;
	}
	public boolean isIs_authorised() {
		return is_authorised;
	}
	public void setIs_authorised(boolean is_authorised) {
		this.is_authorised = is_authorised;
	}
}
