package com.chat.server;

import java.util.ArrayList;

import com.chat.communication.CommunicationChannel;
import com.chat.communication.IncomingCommunication.CommunicationListener;
import com.chat.communication.IncomingNetworkCommunication;
import com.chat.communication.MessageListener;
import com.chat.log.ConsoleLogger;

/**
 * Console server app that can be started from command line (>java com.chat.server.ServerApp [port]).
 * 
 * @author Helena
 *
 */
public class ServerApp {
	
	private static final int PORT_1500 = 1500;

	/**
	 * Start server using command line (>java com.chatdomain.server.ServerApp [port]).
	 * 
	 * @param args arg0 = port; default: 1500
	 */
	public static void main(String[] args) {
		
		int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT_1500;
		
		IncomingNetworkCommunication incomingNetworkCommunication = new IncomingNetworkCommunication(port);
		ConsoleLogger logger = new ConsoleLogger("server");
		MessageListener messageListener = new MessageListener() {
			
			@Override
			public void onMessageReceived(String message) {
				logger.log("Reading line from client: " + message);
			}
		};
		
		ArrayList<ServerConnectionThread> serverConnectionThreads = new ArrayList<ServerConnectionThread>();
		
		ServerImpl server = new ServerImpl(logger, incomingNetworkCommunication, serverConnectionThreads);

		logger.log("Waiting for clients on port " + port + "...");
		
		while (server.isRunning()) {

			incomingNetworkCommunication.listen(new CommunicationListener() {
				
				@Override
				public void onCommunicationChannelOpened(CommunicationChannel clientCommunicationCahnnel) {

					// start new thread after accepting connection with client
					ServerConnectionThread serverConnectionThread = new ServerConnectionThread(logger,
																							   clientCommunicationCahnnel,
																							   serverConnectionThreads,
																							   messageListener);
					server.start(serverConnectionThread);

					
				}
			});
		}

	}

}
