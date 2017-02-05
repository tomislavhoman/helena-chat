package com.chatdomain.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client that is connecting to the server (host and port) and sends messages.
 * 
 * @author Helena
 *
 */
public class Client {
	
	/** Host name.*/
	private String host;
	
	/** Port to which is connecting.*/
	private int port;
	
	/** Client username.*/
	private String username;
	
	/** Client socket.*/
	private Socket socket;
	
	/** Output writer.*/
	private PrintWriter out;
	
	/** Input reader.*/
	private BufferedReader in;
	
	/**
	 * Client with default values localhost 1500 Anonimus.
	 */
	public Client() {
		
		this("localhost", 1500, "Anonimus");
		
	}

	/**
	 * Client with values in parameters.
	 * @param host host
	 * @param port port
	 * @param username username
	 */
	public Client(String host, int port, String username) {
		
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		
	}

	/**
	 * Start client using command line (>java com.chatdomain.client.Client [username] [host] [port]).
	 * 
	 * @param args arg0 - host, arg1 - port, arg2 - username;
	 * default: localhost 1500 Anonimus
	 */
	public static void main(String[] args) {

		String username = args.length > 0 ? args[0] :  "Anonimus";
		
		String host = args.length > 1 ? args[1] : "localhost";
		
		int port = args.length > 2 ? Integer.parseInt(args[2]) : 1500;

		Client client = new Client(host, port, username);
		
		client.start();
		
	}

	/**
	 * Starts the client.
	 */
	public void start() {
		
		try {
			
			System.out.println("Connecting to " + host + " on port " + port);
			
			socket = new Socket(host, port);
			
			System.out.println("User " + username + " connected to server");
			
			out = new PrintWriter(socket.getOutputStream(), true);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			ListenFromServerThread listenFromServerThread = new ListenFromServerThread(in);
			
			listenFromServerThread.start();
			
			// first send your username
			out.println(username);
			
			String userInput;
			
			while ((userInput = stdIn.readLine()) != null) {
				
				sendMessage(userInput);
				
			}

			disconnect();
			
		} catch (Exception e) {

			System.out.println("Could not connect to server: " + host + " on port " + port);
			
		}
		
	}

	/**
	 * Send message from client.
	 * @param message
	 */
	private void sendMessage(String message) {
		
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
