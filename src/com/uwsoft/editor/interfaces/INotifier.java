package com.uwsoft.editor.interfaces;

public interface INotifier {
	public void sendNotification( String notificationName, Object body );
	public void sendNotification( String notificationName );
}
