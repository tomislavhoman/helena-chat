package com.chat.communication;

import java.io.IOException;
import java.net.ServerSocket;

import com.chat.log.ConsoleLogger;

/**
 * Incoming network communication from serverSocket.
 * 
 * @author Helena
 *
 */
public class IncomingNetworkCommunication implements IncomingCommunication {
	
	/** Server socket that listens on a port.*/
	private ServerSocket serverSocket;
	private ConsoleLogger logger;

	public IncomingNetworkCommunication(int port, ConsoleLogger logger) {
		
		this.logger = logger;

		try {
			
			serverSocket = new ServerSocket(port);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void listen(CommunicationListener listener) {
		
		if (serverSocket == null || serverSocket.isClosed()) {
			return;
		}

		try {
			
			CommunicationChannel clientChannel = new NetworkChannel(serverSocket.accept());
			clientChannel.open();

			listener.onCommunicationChannelOpened(clientChannel);

		} catch (Exception e) {
			logger.log("Could not open communication chanel");
		}
		
	}

	@Override
	public void close() {
		
		if (serverSocket == null || serverSocket.isClosed()) {
			return;
		}

		try {
			
			serverSocket.close();
			
		} catch (IOException e) {
			logger.log("Could not close communication chanel");
		}
		
	}

}
