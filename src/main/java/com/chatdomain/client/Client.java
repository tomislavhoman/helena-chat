package com.chatdomain.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client that is connecting to the server (host and port) and sending messages.
 * 
 * @author Helena
 *
 */
public class Client {
	
	/** Host name.*/
	private static String host = "localhost";
	
	/** Port to which is connecting.*/
	private static int port = 1500;
	
	/** Client username.*/
	private static String username = "Anonimus";
	
	/**
	 * Start client using command line.
	 * 
	 * @param args arg0 - host, arg1 - port, arg2 - username;
	 * default: localhost 1500
	 */
	public static void main(String[] args) {
		
		host = args.length > 0 ? args[0] : host;
		
		port = args.length > 1 ? Integer.parseInt(args[1]) : port;

		username = args.length > 2 ? args[2] : username;
		
		startClient();
		
	}

	/**
	 * Starts the client.
	 */
	private static void startClient() {
		
		try {
			
			System.out.println("Connecting to " + host + " on port " + port);
			
			Socket clientSocket = new Socket(host, port);
			
			System.out.println("User " + username + " connected to server");
			
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			out.println(username);
			System.out.println("echo: " + in.readLine());
			System.out.println("Plese input message line:");
			
			String userInput;
			
			while ((userInput = stdIn.readLine()) != null) {

				out.println(userInput);
				System.out.println("echo: " + in.readLine());
				
			}
			
			clientSocket.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
