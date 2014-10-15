package com.uwsoft.editor.interfaces;

public interface IObserver {
	public String getObserverName( ); 
	public String[] listNotificationInterests( );
	public void handleNotification( String notificationName, Object body );
}
