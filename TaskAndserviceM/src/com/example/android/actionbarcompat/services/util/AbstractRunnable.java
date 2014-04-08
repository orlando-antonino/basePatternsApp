package com.example.android.actionbarcompat.services.util;

import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author 
 *
 */
public abstract class AbstractRunnable implements Runnable {
	
	protected Handler handler;
	protected String tag = this.getClass().getName();
	
	public AbstractRunnable(Handler handler) {
		super();
		this.handler = handler;
		
	}

	protected abstract void doRun() throws Exception;
	
	public void run() {
		try {
			doRun();
		}catch(Exception e) {
			Log.i("SipRunnable", e.getMessage());
		}
	}
}