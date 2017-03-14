package com.chat.client;

/**
 * Client interface with which we can communicate with client from outside, so we can use it from any user interface.
 * 
 * @author Helena
 *
 */
public interface Client {
	
	String EXIT = "exit";

	/**
	 * Starts the client.
	 * 
	 * @param host Host name
	 * @param port Port to which is connecting
	 * @param username Client username
	 * @throws Exception if client couldn't start
	 */
	public boolean login(String host, int port, String username);

	/**
	 * Sends message from client.
	 * 
	 * @param message 
	 */
	public void sendMessage(String message);
	
	/**
	 * User logout.
	 */
	public void logout();
	
}
