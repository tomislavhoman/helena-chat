package com.chat.threading;

/**
 * 
 * @author Helena
 *
 */
public class NewThreadScheduler implements Scheduler {

	@Override
	public void dispatch(Action1<Action> action) {
		
		Thread newThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				action.call(new Action() {
					
					@Override
					public void call() {
						// No synchronisation, it spawns when it spawns
					}
				});
			}
		});
		newThread.start();
	}
}
