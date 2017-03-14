package com.chat.threading;

/**
 * 
 * @author Helena
 *
 */
public class ImmediateScheduler implements Scheduler {
	
	private static final Object monitor = new Object();

	@Override
	public void dispatch(Action1<Action> action) {
		
		synchronized (monitor) {
		
			Thread newThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					action.call(new Action() {
						
						@Override
						public void call() {
							synchronized (monitor) {
								monitor.notify();	
							}
						}
					});
				}
			});
			newThread.start();
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
