package com.chat.server;

import java.io.IOException;
import java.util.ArrayList;

import com.chat.client.Client;
import com.chat.communication.CommunicationChannel;
import com.chat.communication.MessageListener;
import com.chat.communication.CommunicationChannel.ReadListener;
import com.chat.log.Logger;

/**
 * For every connection from each client new thread is started.
 * 
 * @author Helena
 *
 */
public class ServerConnectionThread extends Thread {
	
	private final Logger logger;
	
	/** User communication channel.*/
	private CommunicationChannel userChannel;

	/** Save all connected threads.*/
	private ArrayList<ServerConnectionThread> serverConnectionThreads;
	
	private MessageListener messageListener;
	
	public ServerConnectionThread(Logger logger,
								  CommunicationChannel userChannel, 
								  ArrayList<ServerConnectionThread> serverConnectionThreads,
								  MessageListener messageListener) {
		
		super();
		this.logger = logger;
		this.userChannel = userChannel;
		this.serverConnectionThreads = serverConnectionThreads;
		this.messageListener = messageListener;
		
	}
	
	@Override
	public void run() {
		
		if (userChannel == null || messageListener == null) {
			return;
		}

		if (serverConnectionThreads != null) {
			serverConnectionThreads.add(this);
		}
	
		while (userChannel.isOpened()) {
			
			try {
	
				userChannel.listen(new ReadListener() {
					
					@Override
					public void onMessage(String message) {
						
						messageListener.onMessageReceived(message);
						
						if (Client.EXIT.equals(message)) {
							close();
						} else {
							broadcast(message);
						}
						
					}
				});
	
			} catch (IOException e) {
				logger.log("Connection closed " + e.getMessage());
				break;
			}
		}
	}
	
	/**
	 * Broadcast message to all other clients.
	 * @param message 
	 * @throws IOException 
	 */
	private void broadcast(String message) {
		
		if (serverConnectionThreads == null || serverConnectionThreads.size() == 0) {
			return;
		}

		for (ServerConnectionThread serverConnectionThread : serverConnectionThreads) {
			serverConnectionThread.writeMessage(message);
		}
		
	}

	/**
	 * Write message to output.
	 * @param message 
	 * @throws IOException 
	 */
	private void writeMessage(String message) {

		if (userChannel == null) {
			return;
		}
		
		userChannel.write(message);
		
	}

	/**
	 * Close thread and remove it from list.
	 */
	public void close() {

		if (userChannel != null) {
			userChannel.close();
		}
		
		serverConnectionThreads.remove(this);
		
	}

}
