package com.example.android.actionbarcompat.services.util;


import android.os.Handler;
import android.os.Message;

/**
 * 
 * Handler a cui è stata aggiunta un interfaccia per realizzare il meccanismo di callback
 * 
 * @author 
 *
 */
public class ProcessCommunicationHandler extends Handler{

	ICommunicationService comm;

	public ProcessCommunicationHandler(ICommunicationService comm) {
		super();
		this.comm = comm;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
			comm.resultCommand(msg.obj);
	}

}
