package com.example.android.actionbarcompat.services;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.android.actionbarcompat.services.task.FactoryCommand;
import com.example.android.actionbarcompat.services.util.AbstractRunnable;
import com.example.android.actionbarcompat.services.util.Const;
import com.example.android.actionbarcompat.services.util.ProcessCommunicationHandler;
import com.example.android.actionbarcompat.services.util.RemoteControllerWakeLock;
import com.example.android.actionbarcompat.services.util.ICommunicationService;

/**
 * 
 * @author 
 *
 */
public class ControlService extends Service implements ICommunicationService{

	private RemoteControllerWakeLock 			sipWakeLock;
	private static HandlerThread 				executorThread;
	private CommandServiceExecutor 				mExecutor;
	public static final String 					TAG 					= "RemoteControlService";
	static int 									count 					= 0;
	AbstractRunnable 							command;
	Context 									context;
	ThreadPoolExecutor							executor				= null;

	/**
	 * Questo handler riceverà i messaggi dei comandi in coda
	 */
	public Handler handler = new ProcessCommunicationHandler(this);

	public static final String ACTION_EXECUTE_COMMAND 		= "com.example.android.remotecontrol.action.execute";
	public static final String ACTION_CANCEL 				= "com.example.android.remotecontrol.action.cancel";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sipWakeLock = new RemoteControllerWakeLock((PowerManager) getSystemService(Context.POWER_SERVICE));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		int res_id = intent.getIntExtra(Const.SERVICE_ID, -1);
		
		/**
		 * l'handler che processerà il messaggio di ritorno si trova nella classe ProcessCommunicationHandler
		 * l'handler definito in questa classe (CommandServiceExecutor) si occupa solo di schedulare le azioni in modo sequenziale 
		 */
		command = FactoryCommand.getCommand(handler, res_id);
		if(command != null) 
			getExecutorSerial().execute(command);
		
		/**
		 * significa che se si interrompe l'esecuzione del servizio non ripartirà in automatico
		 */
		return START_NOT_STICKY; 
	}

	/**
	 * Creo il Looper che mi consentirà di processare i task in modalità Pipeline
	 * @return
	 */
	private static Looper createLooper() {
		if(executorThread == null) {
			Log.d("test", "Creating new handler thread");
			// ADT gives a fake warning due to bad parse rule.
			executorThread = new HandlerThread("SipService.Executor");
			executorThread.start();
		}
		return executorThread.getLooper();
	}


	/**
	 * handler che reppresenta la coda di processamento dei comandi; man mano che arrivano i comandi dall'interfaccia
	 * li inserisco in coda
	 * 
	 * @author 
	 *
	 */
	public static class CommandServiceExecutor extends Handler {
		WeakReference<ControlService> handlerService;

		CommandServiceExecutor(ControlService s) {
			super(createLooper());
			handlerService = new WeakReference<ControlService>(s);
		}

		/**
		 * aggiunge task alla coda
		 * 
		 * @param task
		 */
		public void execute(Runnable task) {
			ControlService s = handlerService.get();
			if(s != null) {
				s.sipWakeLock.acquire(task);
			}
			Message.obtain(this, 0/* don't care */, task).sendToTarget();
		}

		/**
		 * rimuove tutti gli oggetti dalla coda in attesa di essere utilizzati
		 */
		public void cancelAll(){
			removeCallbacksAndMessages(null);
		}

		/**
		 * processa il task corrente
		 */
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj instanceof Runnable) {
				executeInternal((Runnable) msg.obj);
			} else {
				Log.i("test", "can't handle msg: " + msg);
			}
		}

		private void executeInternal(Runnable task) {
			try {
				task.run();
			} catch (Throwable t) {
				Log.e("Test", "run task: " + task, t);
			} finally {

				ControlService s = handlerService.get();
				if(s != null) {
					s.sipWakeLock.release(task);
				}
			}
		}
	}

	/**
	 * restituisce l'oggetto che implementa la coda dei messaggi
	 * 
	 * @return
	 */
	public CommandServiceExecutor getExecutor() {
		// create mExecutor lazily
		if (mExecutor == null) {
			mExecutor = new CommandServiceExecutor(this);
		}
		return mExecutor;
	}
	
	public ThreadPoolExecutor getExecutorSerial() {
		if (executor == null) {
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Const.NUMBER_OF_THREAD);
		}
		return executor;
	}

	int test=0;

	@Override
	public void resultCommand(Object obj) {
		Toast.makeText(getApplicationContext(), "Finish handler  " + test++, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void clearAll(int what) {
		getExecutor().cancelAll();
		Toast.makeText(getApplicationContext(), "Clear all command ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void genericRes(int errorcode) {
		// TODO Auto-generated method stub
		
	}


}
