package com.chat.communication;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Incoming network communication from serverSocket.
 * 
 * @author Helena
 *
 */
public class IncomingNetworkCommunication implements IncomingCommunication {
	
	/** Server socket that listens on a port.*/
	private ServerSocket serverSocket;

	public IncomingNetworkCommunication(int port) {

		try {
			
			serverSocket = new ServerSocket(port);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void listen(CommunicationListener listener) {
		
		if (serverSocket == null) {
			return;
		}

		try {
			
			CommunicationChannel clientChannel = new NetworkChannel(serverSocket.accept());
			clientChannel.open();

			listener.onCommunicationChannelOpened(clientChannel);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void close() {
		
		if (serverSocket == null) {
			return;
		}

		try {
			
			serverSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
