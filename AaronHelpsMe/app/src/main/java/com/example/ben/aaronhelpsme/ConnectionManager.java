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
import java.util.Iterator;
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
	private int port = 1256;
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
		System.out.println("Attempting to connect to server.");
		connectToServer();
		cache = this.lruCache(100);
		taskQueue = new LinkedBlockingQueue<Runnable>();
		this.pool = Executors.newCachedThreadPool();

		lastComm = System.currentTimeMillis();


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
			if(f.getMarker().equals(m)){
				System.out.println("Found actual flag.");
				return f;
			}
		}
		System.out.println("no flag has ben found :(");
		return null;
	}
	public void startCheckTask(){
		pool.execute(getCheckTask());
	}
	private Runnable getCheckTask() {
		return new Runnable() {

			@Override
			public void run() {
				while(true) {
					Iterator<Map.Entry<Integer, Flag>> it = cache.entrySet().iterator();
					while(it.hasNext()){
						Map.Entry<Integer, Flag> ent = it.next();
						if(ent.getValue().getCreated() + flagLifeTime >= System.currentTimeMillis()) {
							//CodeStuff.flagExpired(f);
							it.remove();
						}
					}
					loadFlags(true);
					try {
						Thread.sleep(1000L*20L);
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
			System.out.println("load true");
			loadFlags(true);
		}
	}
	public void loadFlags(final boolean callAfter) {
	    System.out.println("LOAD FLAGS: " + callAfter);
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
					}else
					if(args.length == 2) {
						System.out.println("There is 2 args.");
						//int length = Integer.parseInt(args[0]);
						String[] flags = args[1].split("\\|");
						System.out.println("fl: " + flags.length);
						for(int x = 1; x <= size; x++) {

							String[] data = flags[x].split(" ");
							double lat = Double.parseDouble(data[0]);
							double lon = Double.parseDouble(data[1]);
							String desc = data[2].replaceAll("_", " ");
							String user = data[3];
							long l = Long.parseLong(data[4]);
							int id = Integer.parseInt(data[5]);
							Flag fl = new Flag(lat, lon,id, user, desc, l);
							//final LatLng sydney = new LatLng(lat, lon);
//							fl.setMarker(new MarkerOptions().position(sydney).title(fl.getDisaster())
//									.icon(getIcon(fl.getDisaster()))
//									.snippet(fl.getComment()));
							flas.add(fl);
							if(!cache.containsKey(id)){
								cache.put(id, fl);
							}
						}
						if(callAfter){
							CodeStuff.FlagsLoaded(flas);
						}

						//read.
						read = null;
						System.out.println("Finished getting flags:" + cache.size());
						return;
					}else {
						System.out.print("Got weird response from server for requesting flags.");
						return;
					}
				} catch (Exception e) {
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
					out.flush();
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					//BE CAREFUL DO SECURE EXECUTION TODO
					while(!read.ready()) {}
					if(read.readLine().equalsIgnoreCase("REQIMG")) {
						out.println("IMG " + img.length);
						out.flush();
						out = null;
						while(!read.ready()){ }
						read.readLine();
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
		if(this.cache.containsKey(id)) {
			if(this.cache.get(id).isntSet()) {
				Runnable r = new Runnable() {

					@Override
					public void run() {
						try {
							System.out.println("requesting img 2");
							PrintWriter out = new PrintWriter(server.getOutputStream());
							out.println("GETIMG " + id);
							out.flush();
							out = null;
							BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
							while(!in.ready()) {}
							System.out.println("requesting img 3");
							String st = in.readLine();
							//in.close();
							in = null;
							if(st.equalsIgnoreCase("null")) {
								System.out.print("got null img from server");
							}else {
								System.out.println("requesting img 4");
								String[] args = st.split(" ");
								int size = Integer.parseInt(args[1]);
								System.out.println(st);
								System.out.println("requesting img 5: " + size);
								byte[] img = new byte[size];
								for(int x = 0; x < size; x++){
									img[x] =(byte)server.getInputStream().read();
									if(x%10000 == 0 ||(size-x <= 1000 && x%1000== 0) || (size-x < 10)){
										System.out.println("got to: " + x + ": " + img[x]);
									}
								}
								System.out.println("actually got image.");
								cache.get(id).setImg(img);
								CodeStuff.imageReceived(id, Flag.getBitmap(img));
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
				CodeStuff.imageReceived(id, Flag.getBitmap(f.getImg()));
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
					System.out.println("=====CONNECTING TO SERVER=======");
					server = new Socket(hostname, port);
					System.out.println("------------connected to server----------------");
					pool.execute(getKeepAliveTask());
					server.setReceiveBufferSize(1024*128);
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
		new Thread(r).start();
	}
	public void appClosed() {
		appClosed = true;
	}
	public void checkUser(final String username,final String password) {
		//System.out.println("checking user");
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
