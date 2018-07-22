package com.example.ben.aaronhelpsme;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.lang.ref.WeakReference;

public class ConnectionManager{
	private Socket server;
	private String hostname = "142.44.210.76";
	private int port = 1255;
	private long lastComm;
	private boolean appClosed = false;
	private static final long commrec = 1000L * 60L;
	private ExecutorService pool;
	private BlockingQueue<Runnable> taskQueue;
	private Future currentTask = null;
	private final long executionTime = 1000L * 15L;
	private long killTask;
	private Map<Integer, Flag> cache;

	public static final long flagLifeTime = 1000L * 60L * 60L * 24L;

	public static String user;

	public ConnectionManager() {
		cache = this.lruCache(100);
		taskQueue = new LinkedBlockingQueue<Runnable>();
		this.pool = Executors.newCachedThreadPool();
		connectToServer();
		lastComm = System.currentTimeMillis();

		System.out.println("Attempting to connect to server.");
	}
	private static WeakReference<MapsActivity> map;
	public static void updateActivity(MapsActivity activity) {
		map = new WeakReference<MapsActivity>(activity);
	}
	public static WeakReference<MapsActivity> getMaps(){
		return map;
	}
	public Flag getFlag(Marker m){
		for(Flag f: cache.values()){
			if(f.getMarker() == m){
				return f;
			}
		}
		return null;
	}
	private Runnable getCheckTask() {
		return new Runnable() {

			@Override
			public void run() {
				while(true) {
					for(Integer i: cache.keySet()) {
						Flag f = cache.get(i);
						if(f.getCreated() + flagLifeTime >= System.currentTimeMillis()) {
							//CodeStuff.flagExpired(f);
							cache.remove(i);
						}
					}
					getFlags();
					try {
						Thread.sleep(1000L*60L*2L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}
	private Runnable getRunner() {
		return new Runnable() {

			@Override
			public void run() {
				while(true) {
					if(currentTask == null && !taskQueue.isEmpty()) {
						currentTask = pool.submit(taskQueue.poll());
						killTask = System.currentTimeMillis() + executionTime;
						System.out.println("Started task.");
					}else if(currentTask != null){
						if(currentTask.isDone()) {
							currentTask = null;
							System.out.println("Finished task.");
						}else if(System.currentTimeMillis() > killTask) {
							currentTask.cancel(true);
							currentTask = null;
							System.out.println("Task killed. :ooooooooooooo");
						}
					}else {
						try {
							Thread.sleep(50L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
	}
	private void addTask(Runnable r) {
		try {
			this.taskQueue.put(r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void getFlags(){
		if(!this.cache.isEmpty()){
			CodeStuff.FlagsLoaded(cache.values());
		}else{
			loadFlags(true);
		}
	}
	public void loadFlags(boolean callAfter) {
	    System.out.println("LOAD FLAGS");
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {

					PrintWriter out = new PrintWriter(server.getOutputStream());
					out.println("FLAGS");
					out.flush();
					out = null;
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					List<Flag> flas = new ArrayList<Flag>();
					while(!read.ready()) {}
					String ret = read.readLine();
					System.out.println("GOt falgs: " + ret);
					String[] args = ret.split(":");
					int size = Integer.parseInt(args[0]);
					if(size == 0){
						System.out.println("There are no flags");
						return;
					}
					if(args.length == 2) {
						int length = Integer.parseInt(args[0]);
						String[] flags = args[1].split("|");
						for(int x = 0; x < length; x++) {
							String[] data = flags[x].split(" ");
							double lat = Double.parseDouble(data[0]);
							double lon = Double.parseDouble(data[1]);
							String desc = data[2].replaceAll("_", " ");
							String user = data[3];
							long l = Long.parseLong(data[4]);
							int id = Integer.parseInt(data[5]);
							Flag fl = new Flag(lat, lon, user, desc, l);
							//final LatLng sydney = new LatLng(lat, lon);

//							fl.setMarker(new MarkerOptions().position(sydney).title(fl.getDisaster())
//									.icon(getIcon(fl.getDisaster()))
//									.snippet(fl.getComment()));
							flas.add(fl);
							cache.put(Integer.valueOf(id), fl);
						}
						CodeStuff.FlagsLoaded(flas);
						//read.
						read = null;
						System.out.println("Finished getting flags");
						return;
					}else {
						System.out.print("Got weird response from server for requesting flags.");
						return;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		};
		this.addTask(r);
	}
	public void newFlag(final String username,final String description,final double lat,final double lon,final byte[] img) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					PrintWriter out = new PrintWriter(server.getOutputStream());
					out.println("NF " + username + " " + lat + " " + lon + " " + description.replaceAll("\\s", "_"));
					//out.close();

					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					//BE CAREFUL DO SECURE EXECUTION TODO
					while(!read.ready()) {}
					if(read.readLine().equalsIgnoreCase("REQIMG")) {
						out.print("IMG " + img.length);
						out.flush();
						out = null;
						server.getOutputStream().write(img);
						System.out.println("Sent image sucessfully.");
						return;
					}else {
						System.out.println("WHYY IS SERVER DOING THIS?!?");
						out.flush();
						out = null;
					}
				
				}catch(Exception e) {
					if(!(e instanceof InterruptedException)) {
						e.printStackTrace();
					}	
				}
			}
		};
		this.addTask(r);
	}
	public void getImage(final int id) {
		if(this.cache.containsKey(Integer.valueOf(id))) {
			if(this.cache.get(Integer.valueOf(id)).isntSet()) {
				Runnable r = new Runnable() {

					@Override
					public void run() {
						try {
							PrintWriter out = new PrintWriter(server.getOutputStream());
							out.println("GETIMG " + id);
							out.flush();
							out = null;
							BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
							while(!in.ready()) {}
							String st = in.readLine();
							//in.close();
							in = null;
							if(st.equalsIgnoreCase("null")) {
								System.out.print("got null img from server");
							}else {
								String[] args = st.split(" ");
								int size = Integer.parseInt(args[1]);
								byte[] img = new byte[size];
								server.getInputStream().read(img);
								cache.get(id).setImg(img);
								//stuff.imageReceived(cache.get(Integer.valueOf(id)), Flag.getBitmap(img));
								img = null;
								//TODO call image received
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};
				this.addTask(r);
			}else {
				Flag f = cache.get(id);
				//stuff.imageReceived(cache.get(Integer.valueOf(id)), Flag.getBitmap(f.getImg()));
			}

		}else{
			System.out.println("Tried to find an image for a flag thats not there.");
		}
	}
	private void connectToServer() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					server = new Socket(hostname, port);
					System.out.println("Connected to server.");
					pool.execute(getKeepAliveTask());
					pool.execute(getCheckTask());
					pool.execute(getRunner());
				}catch(IOException e) {
					e.printStackTrace();
					CodeStuff.cannotConnectToServer();
					System.out.println("Failed to connect to server. Retrying in 3s. ");
					try {
						Thread.sleep(3000L);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					run();
				}
				
			}
		};
		this.pool.execute(r);
	}
	public void appClosed() {
		appClosed = true;
	}
	public void checkUser(final String username,final String password) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					PrintWriter out = new PrintWriter(server.getOutputStream());
					out.println("CHECK " + username + " " + password);
					out.flush();
					out = null;
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					while(true) {
						String s = read.readLine();
						if(s != null) {
							if(s.equalsIgnoreCase("true")) {
								read = null;
								CodeStuff.userVerified();
							}else if(s.equalsIgnoreCase("false")) {
								read = null;
								CodeStuff.userNotVerifed();
							}else {
								read = null;
								System.out.println("got weird response from server.");
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		};
		this.addTask(r);

	}
	public void newUser(final String username,final String password,final String email) {
		System.out.println("user being created");
		if(!verifyEmail(email)) {
			CodeStuff.userFailedRegistration("Invalid email.");
			System.out.println("User failed registration.");
			return;
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("stuff");
					PrintWriter out = new PrintWriter(server.getOutputStream());
					out.println("NU " + username + " " + email + " " + password);
					out.flush();
					out = null;
					System.out.println("Sent data..");
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					while(true) {
						String s = read.readLine();
						if(s != null) {
							System.out.println("Got server response: " + s);
							if(s.equalsIgnoreCase("true")) {
								CodeStuff.userRegistered();
							}else if(s.startsWith("FALSE")) {
								String[] st = s.split(":");
								CodeStuff.userFailedRegistration(st[1]);
								System.out.println("User failed registration: " + st[1]);
							}else {
								System.out.println("got weird response from server.");
							}
							read = null;
							break;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}
			
		};
		this.addTask(r);
	}
	private Runnable getKeepAliveTask() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
							Runnable r = new Runnable() {

								@Override
								public void run() {
									PrintWriter out;
									try {
										out = new PrintWriter(server.getOutputStream(), true);
										out.println("KEP");
										lastComm = System.currentTimeMillis();
										out.flush();
										out = null;
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}	
								}
							};
							addTask(r);
						Thread.sleep(commrec);
						if(appClosed) {
							break;
						}
					}
					server.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	private boolean verifyEmail(String s) {
		int i1 = s.indexOf("@");
		if(i1 < 0) {
			return false;
		}
		int i2 = s.indexOf(".", i1);
		if(i2 < 0) {
			return false;
		}
		return true;
	}
	private <K,V> Map<K,V> lruCache(final int maxSize) {
	    return new LinkedHashMap<K, V>(maxSize*4/3, 0.75f, true) {
	        @Override
	        protected boolean removeEldestEntry(Entry<K, V> eldest) {
	            return size() > maxSize;
	        }
	    };
	}

	
}
