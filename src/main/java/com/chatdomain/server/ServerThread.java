package com.chatdomain.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * For every connection from each client new thread is started.
 * 
 * @author Helena
 *
 */
public class ServerThread extends Thread {
	
	/** Socket on which connection to client is accepted.*/
	private Socket socket;

	/**
	 * Thread for socket.
	 * @param socket socket
	 */
	public ServerThread(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try {

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
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
			
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
