package com.example.ben.aaronhelpsme;

import android.graphics.Bitmap;

public class CodeStuff {
	private static ConnectionManager con = null;
	
	
	//CALL THIS METHOD TO VERIFY USER
	public void verifyUser(String username, String password) {
		getCon().checkUser(username, password);
	}
	//CALL THIS METHOD TO REGISTER A NEW USER
	public void registerUser(String username, String email, String password) {
		getCon().newUser(username, password, email);
	}
	//CALL THIS METHOD WHEN APP IS CLOSED
	public void appClosed() {
		getCon().appClosed();
	}
	//CALL THIS METHOD TO REQUEST AN IMAGE FROM A FLAG
	public void userVerified() {
		//INSERT CODE FOR WHEN USER VERIFIED AND LOGGED IN
	}
	public void userNotVerifed() {
		//INSERT CODE FOR INCORRECT USERNAME OR PASSWORD
	}
	public void cannotConnectToServer() {
		//INSERT CODE FOR WHEN USER CANNOT CONNECT TO SERVER
	}
	public void userRegistered() {
		//CALLED WHEN USER SUCESSFULLY REGISTERED
	}
	public void userFailedRegistration(String error) {
		//CALLED WHEN USER REGISTERATION FAILED (i.e username taken or invalid email or something)
	} 

	
	private ConnectionManager getCon() {
		if(con == null) {
			con =  new ConnectionManager(this);
		}
		return con;
	}
}
