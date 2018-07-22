package com.example.ben.aaronhelpsme;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;
import java.util.Collection;

public class CodeStuff {
	private static ConnectionManager con = null;
	
	private static WeakReference<ShowPhoto> photo;
	public static void setPhotoReference(ShowPhoto p){
		photo = new WeakReference<ShowPhoto>(p);
	}
	private static WeakReference<LogIn> login;
	public static void setLoginReference(LogIn l){
		login = new WeakReference<LogIn>(l);
	}
	private static WeakReference<SignUp> signup;
	public static void setSignupReference(SignUp l){
		signup = new WeakReference<SignUp>(l);
	}

	public static void requestImage(int id){
		getCon().getImage(id);
	}
	//CALL THIS METHOD TO VERIFY USER
	public static void verifyUser(String username, String password) {
		getCon().checkUser(username, password);
	}
	//CALL THIS METHOD TO REGISTER A NEW USER
	public static void registerUser(String username, String email, String password) {
		System.out.println("new userr");
		getCon().newUser(username, password, email);
	}
	//CALL THIS METHOD WHEN APP IS CLOSED
	public static void appClosed() {
		getCon().appClosed();
	}


	public static void imageReceived(int id, Bitmap m){
		if(photo.get() != null){
			photo.get().setImage(m, id);
		}

	}

	public static void userVerified() {
		login.get().userVerified();
		//INSERT CODE FOR WHEN USER VERIFIED AND LOGGED IN
	}
	public static void userNotVerifed() {
		login.get().userNotVerified();
		//INSERT CODE FOR INCORRECT USERNAME OR PASSWORD
	}
	public static void cannotConnectToServer() {
		//INSERT CODE FOR WHEN USER CANNOT CONNECT TO SERVER
		System.out.println("SERVER IS DEAD");
	}
	public static void userRegistered() {
		signup.get().userRegistered();
		//CALLED WHEN USER SUCESSFULLY REGISTERED
	}
	public static void userFailedRegistration(String error) {
		signup.get().userFailedRegistration(error);
		//CALLED WHEN USER REGISTERATION FAILED (i.e username taken or invalid email or something)
	} 
	public static void FlagsLoaded(Collection<Flag> flags){
		//CALLED WHEN FLAGS LOADED
		for(Flag f: flags){
			ConnectionManager.getMaps().get().addMarker(f);
		}
	}
	
	public static ConnectionManager getCon() {
		if(con == null) {
			con =  new ConnectionManager();
		}
		return con;
	}
}
