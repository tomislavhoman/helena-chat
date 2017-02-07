package com.chatdomain.service;

/**
 * Interface with which we can access server domain data from outside, so we can use it from any user interface.
 * 
 * @author Helena
 *
 */
public interface ServerService {
	
	/**
	 * Start server on default port 1500.
	 */
	public void startServer();
	
	/**
	 * Start server on port from parameter.
	 * 
	 * @param port  port; default 1500
	 */
	public void startServer(int port);
	
	/**
	 * Stop server.
	 */
	public void stopServer();
}
