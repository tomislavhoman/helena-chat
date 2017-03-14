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
	 * @param serverConnectionThread 
	 */
	public void start(ServerConnectionThread serverConnectionThread);
	
	/**
	 * Stop server.
	 */
	public void stop();

	/**
	 * 
	 * @return true if server is running, false otherwise
	 */
	public boolean isRunning();
}
