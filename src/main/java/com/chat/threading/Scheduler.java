package com.chat.threading;

/**
 * Scheduler interface.
 * 
 * @author Helena
 *
 */
public interface Scheduler {

	void dispatch(Action1<Action> action);
	
	interface Action1<T> {
		void call(T ready);
	}
	
	interface Action {
		void call();
	}
}
