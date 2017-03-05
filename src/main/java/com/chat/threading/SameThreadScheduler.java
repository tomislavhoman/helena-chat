package com.chat.threading;

/**
 * 
 * @author Helena
 *
 */
public class SameThreadScheduler implements Scheduler {

	@Override
	public void dispatch(Action1<Action> action) {
		action.call(new Action() {

			@Override
			public void call() {
				// No synchronisation, same thread
			}
			
		});
	}

}
