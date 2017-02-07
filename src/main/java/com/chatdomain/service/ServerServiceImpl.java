package com.chatdomain.service;

import com.chatdomain.server.Server;

/**
 * Implementation of {@link ServerService} methods with which we can access server domain data from outside.
 * 
 * @author Helena
 *
 */
public class ServerServiceImpl implements ServerService {
	
	/** Server object.*/
	private Server server;

	@Override
	public void startServer() {
		
		server = new Server();
		
		server.start();
		
	}

	@Override
	public void startServer(int port) {

		server = new Server(port);
		
		server.start();
		
	}

	@Override
	public void stopServer() {
		
		if (server == null) {
			return;
		}
		
		server.stop();
		
	}

}
