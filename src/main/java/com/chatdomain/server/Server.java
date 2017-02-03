package com.chatdomain.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server that waits on the port.
 * 
 * @author Helena
 *
 */
public class Server {
	
	/** Port on which is waiting.*/
	private static int port = 1500;

	/**
	 * Start server using command line.
	 * 
	 * @param args arg0 - port, default: 1500
	 */
	public static void main(String[] args) {
		
		port = args.length > 0 ? Integer.parseInt(args[0]) : port;
		
		startServer();
		
	}

	/**
	 * Starts the server.
	 */
	private static void startServer() {
		
		try {
			
			ServerSocket serverSocket = new ServerSocket(port);
			
			System.out.println("Waiting for client on port " + port + "...");
			
			Socket clinetSocket = serverSocket.accept();

			PrintWriter out = new PrintWriter(clinetSocket.getOutputStream(), true);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clinetSocket.getInputStream()));
			
			String username = in.readLine(); // first line that is sent

			System.out.println("Client " + username + " accapted");
			
			String inputLine;
			String outputLine = "Welcome " + username;
			
			out.println(outputLine);
			
			while ((inputLine = in.readLine()) != null) {

				System.out.println("Reading line from client "+ username +": " + inputLine);
				
				outputLine = "Received " + inputLine;
				
				out.println(outputLine);
				
			}
			
			serverSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


}
