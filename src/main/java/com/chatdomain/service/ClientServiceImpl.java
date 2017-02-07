package com.chatdomain.service;

import com.chatdomain.client.Client;

/**
 * Implementation of {@link ClientService} methods with which we can access client domain data from outside.
 * 
 * @author Helena
 *
 */
public class ClientServiceImpl implements ClientService {
	
	/** Client object.*/
	private Client client;
	
	@Override
	public boolean startClient() {

		client = new Client();
		
		return client.start();
		
	}

	@Override
	public boolean startClient(String host, int port) {

		client = new Client(host, port);
		
		return client.start();
		
	}

	@Override
	public void login(String username) {
		
		if (client == null) {
			return;
		}
		
		client.login(username);
		
	}

	@Override
	public void sendMessage(String message) {

		if (client == null) {
			return;
		}
		
		client.sendMessage(message);
		
	}

	@Override
	public void logout() {

		if (client == null) {
			return;
		}
		
		client.logout();
		
	}

}
