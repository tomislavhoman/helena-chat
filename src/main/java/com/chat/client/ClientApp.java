package com.chat.client;

import com.chat.communication.CommunicationChannel;
import com.chat.communication.CommunicationChannel.ReadListener;
import com.chat.communication.ConsoleChannel;
import com.chat.communication.MessageListener;
import com.chat.communication.NetworkChannel;
import com.chat.log.ConsoleLogger;
import com.chat.threading.NewThreadScheduler;

/**
 * Console client app that can be started from command line (>java com.chat.client.ClientApp [host] [port] [username]).
 * 
 * @author Helena
 *
 */
public class ClientApp {

	private static final String HOST_LOCALHOST = "localhost";
	private static final int PORT_1500 = 1500;
	private static final String USERNAME_ANONIMUS = "Anonymous";

	/**
	 * Start client using command line (>java com.chatdomain.client.ClientApp [host] [port] [username]).
	 * 
	 * @param args arg0 - host, arg1 - port;
	 * default: localhost 1500
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		String host = args.length > 0 ? args[0] : HOST_LOCALHOST;
		int port = args.length > 1 ? Integer.parseInt(args[1]) : PORT_1500;
		String username = args.length > 2 ? args[2] : USERNAME_ANONIMUS;
		
		CommunicationChannel consoleChannel = new ConsoleChannel();
		CommunicationChannel networkChannel = new NetworkChannel(host, port);
		ConsoleLogger logger = new ConsoleLogger("client");
		MessageListener messageListener = new MessageListener() {
			
			@Override
			public void onMessageReceived(String message) {
				logger.log("chat room: " + message);
			}
		};
		
		ClientImpl client = new ClientImpl(logger, networkChannel, messageListener, new NewThreadScheduler());
		client.login(host, port, username);

		while(true) {
			consoleChannel.listen(new ReadListener() {
					
				@Override
				public void onMessage(String message) {

					if (Client.EXIT.equals(message)) {

						client.sendMessage(username + " has logged out");
						client.sendMessage(message);
						client.logout();
						System.exit(0);
					}

					client.sendMessage(message);
					
				}
			});
		}	
	}
}
