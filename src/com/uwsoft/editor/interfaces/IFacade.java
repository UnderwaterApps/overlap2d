package com.uwsoft.editor.interfaces;

public interface IFacade extends INotifier {
	public void notifyObservers( String notificationName, Object body );
	public void addObserver( IObserver observer );
	public IObserver getObserver( String observerName );
	public boolean hasObserver( String observerName );
	public IObserver removeObserver( String observerName );
}
