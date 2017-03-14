package com.chat.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Network channel using socket streams.
 * 
 * @author Helena
 *
 */
public class NetworkChannel extends BaseCommunicationChannel {
	
	/** Network socket.*/
	private Socket socket;
	
	private String host;
	
	private int port;
	
	private boolean isOpened = false;
	
	public NetworkChannel(String host, int port) {
		this.host = host;
		this.port = port;		
	}
	
	public NetworkChannel(Socket socket) {
		this.socket = socket;		
	}
	
	@Override
	public void close() {
		if (!isOpened) {
			throw new RuntimeException("Trying to close multiple times");
		}
		
		super.close();
		
		try {
			this.socket.close();
			this.isOpened = false;
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public void open() {
				
			try {
				if (this.socket == null) {
					this.socket = new Socket(host, port);
				}
				
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
				
				initStreams(input, output);
				this.isOpened = true;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public boolean isOpened() {
		return this.isOpened;
	}
}
