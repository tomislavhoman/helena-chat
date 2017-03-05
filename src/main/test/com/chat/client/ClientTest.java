package com.chat.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.chat.communication.CommunicationChannel;
import com.chat.communication.MessageListener;
import com.chat.log.EmptyLogger;
import com.chat.threading.ImmediateScheduler;

/**
 * 
 * @author Helena
 *
 */
public class ClientTest {

	@Test
	public void clinetSendMessage() {

		//Arrange
		MockChannel mockServerChannel = new MockChannel();
		MockMessageListener mockMessageListener = new MockMessageListener();
		
		ClientImpl client = new ClientImpl(new EmptyLogger(), mockServerChannel, mockMessageListener, new ImmediateScheduler());
		assertTrue(client.login("", 0, "testUser"));
		
		assertEquals("testUser has logged in", mockServerChannel.lastMessage());
		assertEquals(1, mockServerChannel.noMessagesReceived());
		
		String firstMessage = "Client sends first message";
		String secondMessage = "Client sends second message";
		
		//Act 
		client.sendMessage(firstMessage);

		//Assert
		assertEquals(firstMessage, mockServerChannel.lastMessage());
		assertEquals(2, mockServerChannel.noMessagesReceived());
		
		//Act more...
		client.sendMessage(secondMessage);

		//Assert more...
		assertEquals(secondMessage, mockServerChannel.lastMessage());
		assertEquals(3, mockServerChannel.noMessagesReceived());
		
		client.logout();
	}
	
	@Test
	public void clinetReceiveMessage() {	
		
		//Arrange
		MockChannel mockServerChannel = new MockChannel();
		MockMessageListener mockMessageListener = new MockMessageListener();
		
		ClientImpl client = new ClientImpl(new EmptyLogger(), mockServerChannel, mockMessageListener, new ImmediateScheduler());
		assertTrue(client.login("", 0, ""));

		String firstMessage = "Server sends first message";
		String secondMessage = "Server sends second message";
		
		//Act 
		mockServerChannel.putToChannel(firstMessage);
		client.sendMessage("Shouldn't interfere");

		//Assert
		assertEquals(firstMessage, mockMessageListener.lastMessage());
		assertEquals(1, mockMessageListener.noMessagesReceived());
		
		//Act more...
		mockServerChannel.putToChannel(secondMessage);

		//Assert more...
		assertEquals(secondMessage, mockMessageListener.lastMessage());
		assertEquals(2, mockMessageListener.noMessagesReceived());
		
		client.logout();
	}

	private class MockChannel implements CommunicationChannel {
		
		private ReadListener readListener;
		private String lastMessage;
		private int noMessagesReceived = 0;
		private boolean isOpened = false;

		@Override
		public void listen(ReadListener listener) throws IOException {
			this.readListener = listener;
		}
	
		@Override
		public void write(String message) {
			this.lastMessage = message;
			this.noMessagesReceived++;
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
	
}
