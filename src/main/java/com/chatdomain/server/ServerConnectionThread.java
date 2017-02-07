package com.chatdomain.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * For every connection from each client new thread is started.
 * 
 * @author Helena
 *
 */
public class ServerConnectionThread extends Thread {
	
	/** Socket on which connection to client is accepted.*/
	private Socket socket;

	/** Save all connected threads.*/
	private ArrayList<ServerConnectionThread> serverConnectionThreads;
	
	/** Output writer.*/
	private PrintWriter out;
	
	/** Input reader.*/
	private BufferedReader in;
	
	/** Client username that is connected on thread.*/
	private String username;

	/**
	 * Thread for socket connection with client.
	 * @param socket socket
	 * @param serverConnectionThreads all threads
	 */
	public ServerConnectionThread(Socket socket, ArrayList<ServerConnectionThread> serverConnectionThreads) {
		
		super();
		this.socket = socket;
		this.serverConnectionThreads = serverConnectionThreads;
		
	}
	
	@Override
	public void run() {
		
		try {

			serverConnectionThreads.add(this);

			out = new PrintWriter(socket.getOutputStream(), true);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// first line that is sent is username
			username = in.readLine(); 

			System.out.println("Client " + username + " accapted");
			
			out.println("Welcome " + username);
			
			broadcast("New user in chat: " + username);
			
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				
				System.out.println("Reading line from client " + inputLine);
				
				broadcast(inputLine);
				
			}
			
			close();
			
		} catch (IOException e) {

			System.out.println("Could not start thread: " + e.getMessage());
			
		}
		
	}
	
	/**
	 * Broadcast message to all other clients.
	 * @param message 
	 */
	private void broadcast(String message) {
		
		if (serverConnectionThreads == null) {
			return;
		}

		for (ServerConnectionThread serverConnectionThread : serverConnectionThreads) {
			
			serverConnectionThread.writeMessage(message);
				
		}
		
	}

	/**
	 * Write message to output.
	 * @param message 
	 */
	private void writeMessage(String message) {

		if (out == null) {
			return;
		}
		
		out.println(message);
		
	}

	/**
	 * Close thread and remove it from list.
	 */
	public void close() {

		try {
			
			if (socket != null) {
				
				socket.close();
				
			}

			if (out != null) {
				
				out.close();
				
			}

			if (in != null) {
				
				in.close();
				
			}
			
		} catch (Exception e) {

			System.out.println("Exception closing thread " + e.getMessage());
			
		}
		
		System.out.println("User " + username + " has disconnected");

		serverConnectionThreads.remove(this);
		
	}
	

}
