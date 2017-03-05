package com.chat.communication;

import java.io.IOException;

/**
 * Communication channel interface for all types of communications.
 * 
 * @author Helena
 *
 */
public interface CommunicationChannel {
	
	/**
	 * Listener for reading messages.
	 * @author Helena
	 *
	 */
	interface ReadListener {
		
		/**
		 * On new message received.
		 * @param message
		 */
		void onMessage(String message);
		
	}
	
	/**
	 * Read message.
	 * @param listener listener for reading messages
	 * @throws IOException problem reading from input
	 */
	public void listen(ReadListener listener) throws IOException;
	
	/**
	 * Write message.
	 * @param message 
	 * @throws IOException problem writing to output
	 */
	public void write(String message);
	
	/**
	 * Open communication channel.
	 */
	public void open();

	/**
	 * Close communication channel.
	 * @throws IOException problem closing connection
	 */
	public void close();
	
	/**
	 * @return true if channel is opened, false otherwise.
	 */
	public boolean isOpened();
	
}
