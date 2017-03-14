package com.chat.communication;

/**
 * Incoming communication interface.
 * 
 * @author Helena
 *
 */
public interface IncomingCommunication {

	/**
	 * Communication channel listener.
	 * @author Helena
	 *
	 */
	public interface CommunicationListener {
		void onCommunicationChannelOpened(CommunicationChannel communicationCahnnel);
	}

	/**
	 * Listen for new connection.
	 * @param listener
	 */
	public void listen(CommunicationListener listener);

	/**
	 * Close connection .
	 */
	public void close();
	
}
