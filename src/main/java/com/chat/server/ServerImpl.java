package com.chat.server;


import java.util.ArrayList;

import com.chat.communication.IncomingCommunication;
import com.chat.log.Logger;

/**
 * Server implementation that waits on specific port.
 * 
 * @author Helena
 *
 */
public class ServerImpl implements Server {
	
	private final Logger logger;
	
	/** Incoming communication where server waits for clients to connect.*/
	private IncomingCommunication incommingCommunication;

	/** True= server is running, false = server has stopped.*/
	private boolean isRunning = true;
	
	/** Save all connected threads.*/
	private ArrayList<ServerConnectionThread> serverConnectionThreads;
	
	public ServerImpl(Logger logger, 
					  IncomingCommunication incomingCommunication, 
					  ArrayList<ServerConnectionThread> serverConnectionThreads) {
		
		super();
		this.logger = logger;
		this.incommingCommunication = incomingCommunication;
		this.serverConnectionThreads = serverConnectionThreads;
		
	}

	@Override
	public void start(ServerConnectionThread serverConnectionThread) {

		serverConnectionThread.start();
			
	}

	@Override
	public void stop() {
		
		isRunning = false;
		
		try {
			
			incommingCommunication.close();
			
			if (serverConnectionThreads != null && serverConnectionThreads.size() > 0) {
	
				for (ServerConnectionThread serverConnectionThread : serverConnectionThreads) {
					
					serverConnectionThread.close();
					
				}
			}
			
		} catch (Exception e) {
			logger.log("Exception closing server " + e.getMessage());
		}
	}
	
	public boolean isRunning() {
		return isRunning;
	}

}
