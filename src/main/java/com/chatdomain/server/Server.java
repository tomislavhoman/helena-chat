package com.chatdomain.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.chatdomain.service.ServerService;

/**
 * Server that waits on the port.
 * <br>
 * Can be started from command line - see {@link #main(String[])} method or from outside using {@link ServerService} methods.
 * 
 * @author Helena
 *
 */
public class Server {
	
	/** Port on which is waiting.*/
	private int port;
	
	/** True= server is running, false = server has stopped.*/
	private boolean isRunning = true;
	
	/** Save all connected threads.*/
	private ArrayList<ServerConnectionThread> serverConnectionThreads = new ArrayList<ServerConnectionThread>();

	/**
	 * Server on default port 1500.
	 */
	public Server() {
		
		this(1500);
		
	}

	/**
	 * Server on port from parameter.
	 * @param port port
	 */
	public Server(int port) {
		
		super();
		this.port = port;
		
	}

	/**
	 * Start server using command line (>java com.chatdomain.server.Server [port]).
	 * 
	 * @param args arg0 = port; default: 1500
	 */
	public static void main(String[] args) {
		
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 1500;
		
		Server server = new Server(port);
		
		server.start();
		
	}

	/**
	 * Starts the server.
	 */
	public void start() {
		
		try {
			
			ServerSocket serverSocket = new ServerSocket(port);
			
			System.out.println("Waiting for clients on port " + port + "...");
			
			while (isRunning) {
				
				// start new thread after accepting connection with client
				ServerConnectionThread serverConnectionThread = new ServerConnectionThread(serverSocket.accept(),
																						   serverConnectionThreads);
				
				serverConnectionThread.start();
				
			}

			System.out.println("Server stop running");
			
			serverSocket.close();
			
			for (ServerConnectionThread serverConnectionThread : serverConnectionThreads) {
				
				serverConnectionThread.close();
				
			}
			
		} catch (IOException e) {
			
			System.out.println("Could not start server on port: " + port + " " + e.getMessage());
			
		}
		
	}
	
	/**
	 * If needed to stop the server.
	 */
	@SuppressWarnings("resource")
	public void stop() {
		
		isRunning = false;
		
		try {
			
			// connect to itself for exit
			new Socket("localhost", port);
			
		} catch (Exception e) {

			System.out.println("Exception closing server " + e.getMessage());
			
		}
		
	}

}
