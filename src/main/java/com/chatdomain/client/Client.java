package com.chatdomain.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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
	 * Start client using command line (>java Client [host] [port] [username]).
	 * 
	 * @param args arg0 - host, arg1 - port, arg2 - username;
	 * default: localhost 1500 Anonimus
	 */
	public static void main(String[] args) {
		
		String host = args.length > 0 ? args[0] : "localhost";
		
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 1500;

		String username = args.length > 2 ? args[2] :  "Anonimus";
		
		Client client = new Client(host, port, username);
		
		client.startClient();
		
	}

	/**
	 * Starts the client.
	 */
	private void startClient() {
		
		try {
			
			System.out.println("Connecting to " + getHost() + " on port " + getPort());
			
			Socket clientSocket = new Socket(getHost(), getPort());
			
			System.out.println("User " + getUsername() + " connected to server");
			
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// first send your username
			out.println(getUsername());
			System.out.println("echo: " + in.readLine());
			
			System.out.println("Plese input message line:");
			
			String userInput;
			
			while ((userInput = stdIn.readLine()) != null) {

				out.println(userInput);
				System.out.println("echo: " + in.readLine());
				
			}
			
			clientSocket.close();
			
			
		} catch (UnknownHostException e) {

			System.out.println("Could not connect to server: " + getHost() + " on port " + getPort());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
