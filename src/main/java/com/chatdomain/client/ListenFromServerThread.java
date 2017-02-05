package com.chatdomain.client;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Listens for data sent by server.
 * 
 * @author Helena
 *
 */
public class ListenFromServerThread extends Thread {
	
	/** Input reader.*/
	private BufferedReader in;
	
	/**
	 * Thread for listening from server.
	 * @param out output reader
	 * @param in input writer
	 */
	public ListenFromServerThread(BufferedReader in) {
		super();
		this.in = in;
	}

	@Override
	public void run() {

		while (true) {
			
			try {
				
				String message = in.readLine();

				System.out.println(message);
				
			} catch (IOException e) {

				System.out.println("Server connection closed.");
				
				break;
				
			}
		
		}
		
	}

}
