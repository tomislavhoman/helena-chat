package com.chat.server;

/**
 * Server interface with which we can communicate with server from outside, so we can use it from any user interface.
 * 
 * @author Helena
 *
 */
public interface Server {

	/**
	 * Starts the server.
	 * 
	 * @param port port on which server is waiting
	 */
	public void start(int port);
	
	/**
	 * Stop server.
	 */
	public void stop();
}
