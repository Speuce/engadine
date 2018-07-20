package com.example.ben.aaronhelpsme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionManager{
	private Socket server;
	private String hostname = "127.0.0.1";
	private int port = 1255;
	private long lastComm;
	private boolean appClosed = false;
	private static final long commrec = 1000L * 60L;
	private CodeStuff stuff;
	private ExecutorService pool;
	private BlockingQueue<Runnable> taskQueue;
	private Future currentTask = null;
	private final long executionTime = 1000L * 30L;
	private long killTask;
	private Map<Integer, Flag> cache;
	public static final long flagLifeTime = 1000L * 60L * 60L * 24L;
	public ConnectionManager(CodeStuff stuff) {
		this.stuff = stuff;
		//cache = this.lruCache(100);
		taskQueue = new LinkedBlockingQueue<Runnable>();
		this.pool = Executors.newCachedThreadPool();
		connectToServer();
		lastComm = System.currentTimeMillis();
		//this.pool.execute(this.getCheckTask());
	}
//	private Runnable getCheckTask() {
//		return new Runnable() {
//
//			@Override
//			public void run() {
//				while(true) {
//					for(Integer i: cache.keySet()) {
//						Flag f = cache.get(i);
//						if(f.getCreated() + flagLifeTime >= System.currentTimeMillis()) {
//							stuff.flagExpired(f);
//							cache.remove(i);
//						}
//					}
//					getFlags();
//					try {
//						Thread.sleep(1000L*60L*2L);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//	}
	private Runnable getRunner() {
		return new Runnable() {

			@Override
			public void run() {
				while(true) {
					if(currentTask == null && !taskQueue.isEmpty()) {
						currentTask = pool.submit(taskQueue.poll());
						killTask = System.currentTimeMillis() + executionTime;
					}else if(currentTask != null){
						if(currentTask.isDone()) {
							currentTask = null;
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
	
	public void getFlags() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					PrintWriter out = new PrintWriter(server.getOutputStream());
					out.println("FLAGS");
					out.close();
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					while(!read.ready()) {}
					String ret = read.readLine();
					String[] args = ret.split(":");
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
							cache.put(Integer.valueOf(id), new Flag(lat, lon, user, desc, l));
						}
						
					}else {
						System.out.print("Got weird response from server for requesting flags.");
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
						out.close();
						server.getOutputStream().write(img);
						System.out.println("Sent image sucessfully.");
						return;
					}else {
						System.out.println("WHYY IS SERVER DOING THIS?!?");
						out.close();
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
//	public void getImage(final int id) {
//		if(this.cache.containsKey(Integer.valueOf(id))) {
//			if(this.cache.get(Integer.valueOf(id)).isntSet()) {
//				Runnable r = new Runnable() {
//
//					@Override
//					public void run() {
//						try {
//							PrintWriter out = new PrintWriter(server.getOutputStream());
//							out.println("GETIMG " + id);
//							out.close();
//							BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
//							while(!in.ready()) {}
//							String st = in.readLine();
//							in.close();
//							if(st.equalsIgnoreCase("null")) {
//								System.out.print("got null img from server");
//							}else {
//								String[] args = st.split(" ");
//								int size = Integer.parseInt(args[1]);
//								byte[] img = new byte[size];
//								server.getInputStream().read(img);
//								cache.get(id).setImg(img);
//								//stuff.imageReceived(cache.get(Integer.valueOf(id)), Flag.getBitmap(img));
//								img = null;
//								//TODO call image received
//							}
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//					}
//				};
//				this.addTask(r);
//			}else {
//				Flag f = cache.get(id);
//				//stuff.imageReceived(cache.get(Integer.valueOf(id)), Flag.getBitmap(f.getImg()));
//			}
//
//		}else{
//			System.out.println("Tried to find an image for a flag thats not there.");
//		}
//	}
	private void connectToServer() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					server = new Socket(hostname, port);
					System.out.println("Connected to server.");
					pool.execute(getKeepAliveTask());
					pool.execute(getRunner());
				}catch(IOException e) {
					e.printStackTrace();
					stuff.cannotConnectToServer();
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
					out.write("CHECK " + username + " " + password);
					out.close();
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					while(true) {
						String s = read.readLine();
						if(s != null) {
							if(s.equalsIgnoreCase("true")) {
								read.close();
								stuff.userVerified();
							}else if(s.equalsIgnoreCase("false")) {
								read.close();
								stuff.userNotVerifed();
							}else {
								read.close();
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
		if(!verifyEmail(email)) {
			this.stuff.userFailedRegistration("Invalid email.");
			return;
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					PrintWriter out = new PrintWriter(server.getOutputStream());
					out.write("NU " + username + " " + email + " " + password);
					out.close();
					BufferedReader read = new BufferedReader(new InputStreamReader(server.getInputStream()));
					while(true) {
						String s = read.readLine();
						if(s != null) {
							if(s.equalsIgnoreCase("true")) {
								stuff.userRegistered();
							}else if(s.startsWith("FALSE")) {
								String[] st = s.split(":");
								stuff.userFailedRegistration(st[1]);
							}else {
								System.out.println("got weird response from server.");
							}
							read.close();
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
					if(System.currentTimeMillis() - lastComm >= commrec) {
							Runnable r = new Runnable() {

								@Override
								public void run() {
									PrintWriter out;
									try {
										out = new PrintWriter(server.getOutputStream(), true);
										out.println("KEP");
										lastComm = System.currentTimeMillis();
										out.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}	
								}
							};
							addTask(r);
						}
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
