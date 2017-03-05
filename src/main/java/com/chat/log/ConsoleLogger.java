package com.chat.log;

public class ConsoleLogger implements Logger {
	
	private final String tag;
	
	public ConsoleLogger(String tag) {
		this.tag = tag;
	}

	@Override
	public void log(String message) {
		System.out.println(String.format("%s:%s", tag, message));
	}

}
