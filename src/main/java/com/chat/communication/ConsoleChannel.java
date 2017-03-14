package com.chat.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Console channel using standard input and output.
 * 
 * @author Helena
 *
 */
public class ConsoleChannel extends BaseCommunicationChannel {

	public ConsoleChannel() {
		
		initStreams(new BufferedReader(new InputStreamReader(System.in)),  new PrintWriter(System.out));
		
	}

	@Override
	public boolean isOpened() {
		return true;
	}
}
