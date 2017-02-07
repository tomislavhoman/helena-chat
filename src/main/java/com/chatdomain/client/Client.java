package com.chatdomain.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.chatdomain.service.ClientService;

/**
 * Client that is connecting to the server (host and port) and sends messages.
 * <br>
 * Can be started from command line - see {@link #main(String[])} method or from outside using {@link ClientService} methods.
 * 
 * @author Helena
 *
 */
public class Client {
	
	/** Host name.*/
	private String host;
	
	/** Port to which is connecting.*/
	private int port;
	
	/** Client username, if not logged in default 'Anonymous'.*/
	private String username = "Anonymous";
	
	/** Client socket.*/
	private Socket socket;
	
	/** Output writer.*/
	private PrintWriter out;
	
	/** Input reader.*/
	private BufferedReader in;
	
	/** Standard input.*/
	private BufferedReader stdIn;
	
	/**
	 * Client with default values localhost 1500.
	 */
	public Client() {
		
		this("localhost", 1500);
		
	}

	/**
	 * Client with values in parameters.
	 * @param host host
	 * @param port port
	 * @param username username
	 */
	public Client(String host, int port) {
		
		super();
		this.host = host;
		this.port = port;
		
	}

	/**
	 * Start client using command line (>java com.chatdomain.client.Client [host] [port]).
	 * 
	 * @param args arg0 - host, arg1 - port;
	 * default: localhost 1500
	 */
	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "localhost";
		
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 1500;

		Client client = new Client(host, port);
		
		// Noting to do if client didn't start
		if (!client.start()) {
			return;
		}
		
		System.out.println("Input your name:");
		
		try {
		
			// first send your username
			client.login(client.stdIn.readLine());
			
			String userInput;
			
			while ((userInput = client.stdIn.readLine()) != null) {
				
				client.sendMessage(client.username + ": " + userInput);
				
			}
	
		} catch (IOException e) {
			
			System.out.println("Error reading line " + e.getMessage());
			
		}

		client.logout();
		
		client.disconnect();
		
	}

	/**
	 * Starts the client.
	 * @return true if client successfully started, false otherwise
	 */
	public boolean start() {
		
		try {
			
			System.out.println("Connecting to " + host + " on port " + port);
			
			socket = new Socket(host, port);
			
			System.out.println("Connected to server");
			
			out = new PrintWriter(socket.getOutputStream(), true);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			ListenFromServerThread listenFromServerThread = new ListenFromServerThread(in);
			
			listenFromServerThread.start();
			
		} catch (Exception e) {

			System.out.println("Could not connect to server: " + host + " on port " + port + " " + e.getMessage());
			
			return false;
			
		}

		return true;

	}

	/**
	 * Login with username.
	 * @param username 
	 */
	public void login(String username) {
		
		// if it wasn't sent it stays 'Anonymous'
		if (username != null && username.length() > 0) {
		
			this.username = username;
		
		}
		
		sendMessage(this.username);
		
	}

	/**
	 * Logout from chat.
	 */
	public void logout() {
		
		sendMessage(username + " left chat");
		
	}
	/**
	 * Send message from client.
	 * @param message
	 */
	public void sendMessage(String message) {
		
		if (out == null) {
			return;
		}
		
		out.println(message);
		
	}

	/** 
	 * Disconnect client.
	 */
	private void disconnect() {

		try {
			
			if (socket != null) {
				socket.close();
			}

			if (in != null) {
				in.close();
			}

			if (out != null) {
				out.close();
			}
			
		} catch (Exception e) {

			System.out.println("Exception disconnecting client " + e.getMessage());
			
		}

		System.out.println("User " + username + " has disconnected");
		
	}

}
