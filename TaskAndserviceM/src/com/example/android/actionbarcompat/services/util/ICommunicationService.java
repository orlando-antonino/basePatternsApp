package com.example.android.actionbarcompat.services.util;

public interface ICommunicationService {
	/*
	 * Accodare la firma della callback da utilizzare nel codice 
	 */
	public void resultCommand(Object obj);
	public void clearAll(int what);
	public void genericRes(int errorcode);
	

}
