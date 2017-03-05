package com.chat.server;


import java.util.ArrayList;

import com.chat.communication.CommunicationChannel;
import com.chat.communication.IncomingCommunication;
import com.chat.communication.IncomingCommunication.CommunicationListener;
import com.chat.communication.MessageListener;
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
	private ArrayList<ServerConnectionThread> serverConnectionThreads = new ArrayList<ServerConnectionThread>();
	
	/** Listener for messages.*/
	private MessageListener messageListener;
	
	public ServerImpl(Logger logger, 
					  IncomingCommunication incomingCommunication, 
					  MessageListener messageListener) {
		
		super();
		this.logger = logger;
		this.incommingCommunication = incomingCommunication;
		this.messageListener = messageListener;
		
	}

	@Override
	public void start(int port) {

		logger.log("Waiting for clients on port " + port + "...");
		
		while (isRunning) {

			incommingCommunication.listen(new CommunicationListener() {
				
				@Override
				public void onCommunicationChannelOpened(CommunicationChannel clientCommunicationCahnnel) {
		
					// start new thread after accepting connection with client
					ServerConnectionThread serverConnectionThread = new ServerConnectionThread(logger,
																							   clientCommunicationCahnnel,
																							   serverConnectionThreads,
																							   messageListener);
					serverConnectionThread.start();
					
				}
			});
		}
			
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

}
