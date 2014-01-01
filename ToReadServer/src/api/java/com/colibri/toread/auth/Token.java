package com.colibri.toread.auth;

import com.colibri.toread.ReadProperties;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Token {
	private String secureToken;
	
	public void generateToken(){
		this.secureToken = generateCookie();
	}
	
	public boolean validateToken(String testToken){
		if(testToken.compareTo(this.secureToken)==0)
			return true;
		else
			return false;
	}
	
	private String generateCookie() {
		// Create a random authentication token to be used with the device, in
		// the same way we create a salt for passwords
		SecureRandom randomSalt = new SecureRandom();
		byte[] salt = new byte[Integer.parseInt(ReadProperties
				.getProperty("salt_bytes")) * 2];
		randomSalt.nextBytes(salt);

		BigInteger bigInt = new BigInteger(1, salt);
		String hex = bigInt.toString(16);
		int paddingLength = (salt.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}
}
