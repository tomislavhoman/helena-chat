package com.chat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.chat.communication.CommunicationChannel;
import com.chat.communication.CommunicationChannel.ReadListener;
import com.chat.communication.IncomingCommunication;
import com.chat.communication.IncomingCommunication.CommunicationListener;
import com.chat.communication.MessageListener;
import com.chat.log.EmptyLogger;
import com.chat.log.Logger;

/**
 * 
 * @author Helena
 *
 */
public class ServerTest {

	@Test
	public void serverReceiveMessage() {

		//Arrange
		MockChannel clientChannel = new MockChannel();
		EmptyLogger logger = new EmptyLogger();
		MockMessageListener mockMessageListener = new MockMessageListener();
		MockIncomingCommunication mockIncomingCommunication = new MockIncomingCommunication(clientChannel);
		
		ArrayList<ServerConnectionThread> serverConnectionThreads = new ArrayList<ServerConnectionThread>();
		
		ServerImpl server = new ServerImpl(logger, mockIncomingCommunication, serverConnectionThreads);

		mockIncomingCommunication.listen(new CommunicationListener() {
			
			@Override
			public void onCommunicationChannelOpened(CommunicationChannel clientCommunicationCahnnel) {

				MockServerConnectionThread mockServerConnectionThread = new MockServerConnectionThread(logger,
																						   			   clientChannel,
																						   			   serverConnectionThreads,
																						   			   mockMessageListener);
				
				mockServerConnectionThread.run();
				
			}
		});
		
		assertTrue(true);

		String firstMessage = "Client sends first message";
		String secondMessage = "Client sends second message";
		
		//Act 
		clientChannel.putToChannel(firstMessage);
		
		//Assert
		assertEquals(firstMessage, mockMessageListener.lastMessage());
		assertEquals(1, mockMessageListener.noMessagesReceived());

		//Act more...
		clientChannel.putToChannel(secondMessage);

		//Assert more...
		assertEquals(secondMessage, mockMessageListener.lastMessage());
		assertEquals(2, mockMessageListener.noMessagesReceived());
		
		server.stop();
		
	}
	
	private class MockIncomingCommunication implements IncomingCommunication {

		private MockChannel clientChannel;

		public MockIncomingCommunication(MockChannel clientChannel) {
			this.clientChannel = clientChannel;
		}

		@Override
		public void listen(CommunicationListener listener) {
			
			clientChannel.open();
			listener.onCommunicationChannelOpened(clientChannel);
		}
		
		@Override
		public void close() {
			clientChannel.close();
		}

	}

	private class MockChannel implements CommunicationChannel {
		
		private ReadListener readListener;
		private boolean isOpened = false;

		@Override
		public void listen(ReadListener listener) throws IOException {
			this.readListener = listener;
		}
	
		@Override
		public void write(String message) {
			// no write
		}
	
		@Override
		public void close() {
			if (!isOpened) {
				throw new RuntimeException("Trying to close multiple times");
			}
			
			this.isOpened = false;
		}
		
		/**
		 * Put message to channel.
		 * 
		 * @param message
		 */
		private void putToChannel(String message) {
			if (readListener != null) {
				readListener.onMessage(message);
			}
		}

		@Override
		public void open() {
			this.isOpened = true;
			
		}

		@Override
		public boolean isOpened() {
			return this.isOpened;
		}
	}
	

	private class MockMessageListener implements MessageListener {

		private String lastMessage;
		private int noMessagesReceived = 0;
		
		@Override
		public void onMessageReceived(String message) {
			this.lastMessage = message;
			noMessagesReceived++;
		}
		
		/**
		 * 
		 * @return last received message
		 */
		private String lastMessage() {
			return lastMessage;
		}
		
		/**
		 * 
		 * @return number of messages received
		 */
		private int noMessagesReceived() {
			return noMessagesReceived;
		}
	}
	
	private class MockServerConnectionThread extends ServerConnectionThread {
		
		CommunicationChannel userChannel;
		ArrayList<ServerConnectionThread> serverConnectionThreads;
		MessageListener messageListener;
		
		public MockServerConnectionThread(Logger logger, 
										  CommunicationChannel userChannel,
										  ArrayList<ServerConnectionThread> serverConnectionThreads, 
										  MessageListener messageListener) {
			super(logger, userChannel, serverConnectionThreads, messageListener);
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

			try {
				
				userChannel.listen(new ReadListener() {
					
					@Override
					public void onMessage(String message) {
						
						messageListener.onMessageReceived(message);
						
					}
				});
				
			} catch (IOException e) {
			}
		}
		
	}
	
}
