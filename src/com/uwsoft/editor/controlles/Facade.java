package com.uwsoft.editor.controlles;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.uwsoft.editor.interfaces.IFacade;
import com.uwsoft.editor.interfaces.IObserver;

public class Facade implements IFacade {
	

	private Hashtable<String,List<IObserver>> observersMapByNotifications;
	private Hashtable<String,IObserver> observerMap;
	
	public Facade(){
		observersMapByNotifications = new Hashtable<String, List<IObserver>>(1);
		observerMap = new Hashtable<String, IObserver>(1);
	}
	
	@Override
	public void sendNotification(String notificationName, Object body) {
		notifyObservers(notificationName, body);
	}

	@Override
	public void sendNotification(String notificationName) {
		sendNotification(notificationName, null);
	}

	@Override
	public void notifyObservers(String notificationName, Object body) {
		List<IObserver> observers_ref = (List<IObserver>) observersMapByNotifications.get(notificationName);
		if( observers_ref != null )
		{
            
			// Copy observers from reference array to working array,
            // since the reference array may change during the
            //notification loop
			Object[] observers = (Object[])observers_ref.toArray();
			
			// Notify Observers from the working array
			for( int i=0; i<observers.length; i++ )
			{
				IObserver observer = (IObserver)observers[i];
				observer.handleNotification(notificationName, body);
			}
		}
	}

	@Override
	public synchronized void addObserver(IObserver observer) {
		if( this.observerMap.containsKey(observer.getObserverName()) )
			return;

		// Register the Mediator for retrieval by name
		this.observerMap.put(observer.getObserverName(), observer);

		// Get Notification interests, if any.
		String[] noteInterests = observer.listNotificationInterests();
		if (noteInterests.length != 0)
		{
			// Register Mediator as Observer for its list of Notification
			// interests
			for (int i = 0; i < noteInterests.length; i++){
				if( this.observersMapByNotifications.get(noteInterests[i]) == null )
					this.observersMapByNotifications.put(noteInterests[i], new ArrayList<IObserver>());

				List<IObserver> observers = (List<IObserver>) this.observersMapByNotifications.get(noteInterests[i]);
				observers.add(observer);
			}
				//registerObserver(noteInterests[i], observer);
		}
	}

	@Override
	public IObserver getObserver(String observerName) {
		return this.observerMap.get(observerName);
	}

	@Override
	public boolean hasObserver(String observerName) {
		return observerMap.containsKey(observerName);
	}

	@Override
	public IObserver removeObserver(String observerName) {
		// Retrieve the named mediator
		IObserver observer = observerMap.get(observerName);
		
		if( observer!=null ) 
		{
			// for every notification this mediator is interested in...
			String[] interests = observer.listNotificationInterests();
			for( int i=0; i<interests.length; i++ ) 
			{
				// remove the observer linking the mediator 
				// to the notification interest
				removeObserverFromNotificationsMap( interests[i], observer );
			}	
			
			// remove the mediator from the map		
			observerMap.remove( observerName );
			return observer;
		}
		return null;
	}

	private void removeObserverFromNotificationsMap(String notificationName,IObserver observer) {
		// the observer list for the notification under inspection
		List<IObserver> observers = observersMapByNotifications.get(notificationName);

		if( observers != null )
		{
			// find the observer for the notifyContext
			for( int i=0; i<observers.size(); i++ )
			{
				IObserver tmpObserver = (IObserver) observers.get(i);
				if(tmpObserver.getObserverName().equalsIgnoreCase(observer.getObserverName())){
					observers.remove(i);
					break;
				}
			}
			
			// Also, when a Notification's Observer list length falls to
			// zero, delete the notification key from the observer map
			if( observers.size() == 0 )
				observersMapByNotifications.remove(notificationName);
		}
	}
	
	
}
