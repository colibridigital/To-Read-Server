package com.colibri.toread.auth;

import java.util.ArrayList;
import java.util.Date;

import com.colibri.toread.ToReadBaseEntity;

public class User extends ToReadBaseEntity {
	private String user_name;
	private String email_address;
	private String password;
	private ArrayList<Device> devices = new ArrayList<Device>(); //All of a users registered devices
	private String first_name;
	private String last_name;
	private Date dob;
}
