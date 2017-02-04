package com.chatdomain.server;


import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server that waits on the port.
 * 
 * @author Helena
 *
 */
public class Server {
	
	/** Port on which is waiting.*/
	private int port;
	
	/** True= server is running, false = server has stopped.*/
	private boolean isRunning = true;

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
	 * @param args arg0 = port, default: 1500
	 */
	public static void main(String[] args) {
		
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 1500;
		
		Server server = new Server(port);
		
		server.startServer();
		
	}

	/**
	 * Starts the server.
	 */
	private void startServer() {
		
		try {
			
			ServerSocket serverSocket = new ServerSocket(getPort());
			
			System.out.println("Waiting for client on port " + getPort() + "...");
			
			while (isRunning) {
				
				// start new thread after accepting connection with client
				ServerThread serverThread = new ServerThread(serverSocket.accept());
				
				serverThread.start();
			
			}
			
			serverSocket.close();
			
		} catch (IOException e) {
			
			System.out.println("Could not start server on port: " + getPort());
			
		}
		
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
}
