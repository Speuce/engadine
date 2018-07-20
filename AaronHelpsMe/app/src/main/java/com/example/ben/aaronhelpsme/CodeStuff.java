package com.example.ben.aaronhelpsme;

import android.graphics.Bitmap;

public class CodeStuff {
	
	
	//CALL THIS METHOD TO VERIFY USER
	public static void verifyUser(String username, String password) {
		ConnectionManager.getConnection().checkUser(username, password);
	}
	//CALL THIS METHOD TO REGISTER A NEW USER
	public static void registerUser(String username, String email, String password) {
		ConnectionManager.getConnection().newUser(username, password, email);
	}
	//CALL THIS METHOD WHEN APP IS CLOSED
	public static void appClosed() {
		ConnectionManager.getConnection().appClosed();
	}
	//CALL THIS METHOD TO REQUEST AN IMAGE FROM A FLAG
	public static void requestImage(Flag f) {
		ConnectionManager.getConnection().getImage(f.getId());
	}
	
	public static void imageReceived(Flag f, Bitmap b) {

	}
	public static void flagExpired(Flag f) {
		//INSERT CODE FOR WHEN FLAG IS DELETED/EXPIRED
	}
	public static void userVerified() {
		//INSERT CODE FOR WHEN USER VERIFIED AND LOGGED IN
	}
	public static void userNotVerifed() {
		//INSERT CODE FOR INCORRECT USERNAME OR PASSWORD
	}
	public static void cannotConnectToServer() {
		//INSERT CODE FOR WHEN USER CANNOT CONNECT TO SERVER
	}
	public static void userRegistered() {
		//CALLED WHEN USER SUCESSFULLY REGISTERED
	}
	public static void userFailedRegistration(String error) {
		//CALLED WHEN USER REGISTERATION FAILED (i.e username taken or invalid email or something)
	}
}
