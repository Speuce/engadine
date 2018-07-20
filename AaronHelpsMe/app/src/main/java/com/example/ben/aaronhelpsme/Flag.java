package com.example.ben.aaronhelpsme;

import android.graphics.*;
import java.nio.ByteBuffer;

public class Flag {
	private double lat, lon;
	private int veri, id;
	private String user, desc;
	private byte[] img;
	private boolean isntSet;
	private long created;
	public Flag(double lat, double lon, int veri, int id, String user, byte[] img, String desc, long created) {
		this.lat = lat;
		this.lon = lon;
		this.veri = veri;
		this.id = id;
		this.user = user;
		this.img = img;
		this.desc =desc;
	}
	public Flag(double lat, double lon, String user, String desc, long created) {
		this.isntSet = true;
		this.lat = lat;
		this.lon = lon;
		this.user = user;
		this.desc = desc;
		this.created = created;
	}
	public boolean isntSet() {
		return this.isntSet;
	}
	public long getCreated() {
		return this.created;
	}
	@Deprecated
	public void setImg(byte[] img) {
		this.img = img;
	}
	public String getDescription() {
		return this.desc;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public int getVeri() {
		return veri;
	}
	public int getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public byte[] getImg() {
		return img;
	}
	public static Bitmap getBitmap(byte[] b){
		//byte[] bitmapdata; // let this be your byte array
		return BitmapFactory.decodeByteArray(b , 0, b.length);
	}
	public static byte[] getByteArray(Bitmap m){
		int bytes =m.getByteCount();
		ByteBuffer buffer = ByteBuffer.allocate(bytes);
		m.copyPixelsToBuffer(buffer);
		return buffer.array();
	}

}