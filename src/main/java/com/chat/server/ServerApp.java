package com.chat.server;

import com.chat.communication.IncomingNetworkCommunication;
import com.chat.communication.MessageListener;
import com.chat.log.ConsoleLogger;
import com.chat.threading.NewThreadScheduler;

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
		
		ServerImpl server = new ServerImpl(logger, incomingNetworkCommunication, messageListener);

		server.start(port);

	}

}
