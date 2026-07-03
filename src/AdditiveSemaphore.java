package com.pwmanager.pwmanager;

public class AdditiveSemaphore {
	private int value;
	public AdditiveSemaphore(int init) {
		if (init < 0)
		init = 0;
		this.value = init;
	}
	public synchronized void p(int x) {
		if (x <= 0)
			return;
		while(value-x<0) {
			try {
				wait();
			}
			catch (InterruptedException e) {
			}
		}
		value -= x;
	}
	
	public synchronized void v(int x) {
		if (x <= 0)
			return;
		value += x;
		notifyAll(); //nicht notify
	}
	
	public void p() {
		p(1);
	}

	public void v() {
		v(1);
	}
	
	public void change(int x) {
		if (x > 0)
			v(x);
		else
			if (x < 0)
				p(-x);
	}
}
