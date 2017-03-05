package com.chat.client;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chat.communication.CommunicationChannel;
import com.chat.communication.CommunicationChannel.ReadListener;
import com.chat.communication.MessageListener;
import com.chat.log.Logger;
import com.chat.threading.Scheduler;
import com.chat.threading.Scheduler.Action;
import com.chat.threading.Scheduler.Action1;

/**
 * Client implementation that is connecting to the server (host and port) and sends messages.
 * 
 * @author Helena
 *
 */
public class ClientImpl implements Client {
	
	private final Logger logger;
	
	/** Server communication channel.*/
	private final CommunicationChannel serverChannel;
	
	private final MessageListener messageListener;
	
	private final Scheduler scheduler;
	
	private AtomicBoolean isListening = new AtomicBoolean(false);// TODO Volatile could also probably do
	
	/** Client username.*/
	private String username;
	
	public ClientImpl(Logger logger, 
					  CommunicationChannel serverChannel, 
					  MessageListener messageListener,
					  Scheduler scheduler) {
		
		super();
		this.logger = logger;
		this.serverChannel = serverChannel;
		this.messageListener = messageListener;
		this.scheduler = scheduler;
	}

	@Override
	public boolean login(String host, int port, String username) {
		
		try {
			
			this.username = username;
			this.scheduler.dispatch(new Action1<Action>() {

				@Override
				public void call(Action ready) {
					listenServer(ready);
				}
			});

			logger.log("connected to server: " + host + " on port " + port);
			return true;
			
		} catch (Exception e) {
			logger.log("Could not connect to server: " + host + " on port " + port + " " + e.getMessage());
			return false;
		}

	}
	
	/**
	 * Open server channel and start listening.
	 * 
	 * @param ready 
	 */
	private void listenServer(Action ready) {
		
		if (messageListener == null || serverChannel == null) {
			return;
		}

		serverChannel.open();
		isListening.set(true);
		
		sendMessage(username + " has logged in");
		
		while (isListening.get()) {
			
			try {
				
				serverChannel.listen(new ReadListener() {
					
					@Override
					public void onMessage(String message) {
							
						messageListener.onMessageReceived(message);
					}
				});
				ready.call();
				
			} catch (IOException e) {
				logger.log("Connection closed " + e.getMessage());
				break;
			}
		}
	}

	/**
	 * Logout from chat.
	 */
	@Override
	public void logout() {
		
		if (serverChannel != null) {
			serverChannel.close();
		}
		isListening.set(false);
	}
	
	/**
	 * Send message from client.
	 * @param message
	 */
	@Override
	public void sendMessage(String message) {
		
		if (serverChannel != null) {
			serverChannel.write(message);
		}
		
	}
	
}
