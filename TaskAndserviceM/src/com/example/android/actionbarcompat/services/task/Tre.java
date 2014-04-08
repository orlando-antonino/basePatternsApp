package com.example.android.actionbarcompat.services.task;

import android.os.Handler;
import android.util.Log;

import com.example.android.actionbarcompat.services.util.AbstractRunnable;

/**
 * 
 * @author 
 *
 */
public class Tre extends AbstractRunnable{
	
	public Tre(Handler handler) {
		super(handler);
	}

	@Override
	protected void doRun() throws Exception {
		
		Log.i(tag, " "+ tag + "  this: " + this);
		
		/**
		 * QUI VERRA' INSERITA LA COMUNICAZIONE COL SERVER UPNP
		 */
		
		Thread.sleep(3000);
		/**
		 * questo handler è dichiarato nella classe padre e lo uso per mandare i mess al servizio che li processa
		 */
		handler.obtainMessage(1).sendToTarget();
	}
	
}