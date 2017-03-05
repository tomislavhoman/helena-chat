package com.chat.communication;

/**
 * Message listener interface.
 * 
 * @author Helena
 *
 */
public interface MessageListener {
	
	/**
	 * 
	 * @param message 
	 */
	void onMessageReceived(String message);
	
}
