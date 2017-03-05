package com.chatdomain.service;

/**
 * Interface with which we can access client domain data from outside, so we can use it from any user interface.
 * 
 * @author Helena
 *
 */
public interface ClientService {
	
	/**
	 * Start client on default host 'localhost' and port 1500.
	 * 
	 * @return true if client successfully started, false otherwise
	 */
	public boolean startClient();
	
	/**
	 * Start client with parameters for host and port.
	 * 
	 * @param host host; default 'localhost'
	 * @param port port; default 1500
	 * @return true if client successfully started, false otherwise
	 * 
	 */
	public boolean startClient(String host, int port);
	
	/**
	 * Login with username.
	 * If username not set, default will be 'Anonimus'.
	 * 
	 * @param username 
	 */
	public void login(String username);
	
	/**
	 * Send message from client.
	 * 
	 * @param message 
	 */
	public void sendMessage(String message);
	
	/**
	 * User logout.
	 */
	public void logout();
	
}
