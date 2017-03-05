package com.chat.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Basic communication channel - abstract, specific channel implementation in subclasses.
 * 
 * @author Helena
 *
 */
public abstract class BaseCommunicationChannel implements CommunicationChannel {
	
	/** Input reader.*/
	private BufferedReader input;
	
	/** Output writer.*/
	private PrintWriter output;
	
	public BaseCommunicationChannel() {
	}
	
	protected void initStreams(BufferedReader input, PrintWriter output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void listen(ReadListener listener) throws IOException {

		if (input == null || listener == null) {
			return;
		}
		
		listener.onMessage(input.readLine());
		
	}

	@Override
	public void write(String message) {

		if (output == null) {
			return;
		}
		
		output.println(message);
		
	}

	@Override
	public void close() {

		try {
			
			input.close();
			output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void open() {
		// NO-OP
	}

}
