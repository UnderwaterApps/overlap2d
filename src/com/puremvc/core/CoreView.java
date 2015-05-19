/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */
package com.puremvc.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.observer.BaseObserver;
import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Observer;

/**
 * A Singleton <code>IView</code> implementation.
 * <p>
 * <p>
 * In PureMVC, the <code>View</code> class assumes these responsibilities:
 * <UL>
 * <LI>Maintain a cache of <code>IMediator</code> instances.</LI>
 * <LI>Provide methods for registering, retrieving, and removing
 * <code>IMediators</code>.</LI>
 * <LI>Managing the observer lists for each <code>INotification</code> in the
 * application.</LI>
 * <LI>Providing a method for attaching <code>IObservers</code> to an
 * <code>INotification</code>'s observer list.</LI>
 * <LI>Providing a method for broadcasting an <code>INotification</code>.</LI>
 * <LI>Notifying the <code>IObservers</code> of a given
 * <code>INotification</code> when it broadcast.</LI>
 * </UL>
 *
 * @see com.puremvc.patterns.mediator.Mediator Mediator
 * @see com.puremvc.patterns.observer.Observer Observer
 * @see com.puremvc.patterns.observer.Notification Notification
 */
public class CoreView implements View {

    // Singleton instance
    private static CoreView instance;
    // Mapping of Mediator names to Mediator instances
    // Mapping of Notification names to Observer lists
    private HashMap<String, List<Observer>> observerMap;
    private HashMap<String, Mediator> mediatorMap;

    /**
     * Constructor.
     * <p>
     * <p>
     * This <code>IView</code> implementation is a Singleton, so you should
     * not call the constructor directly, but instead call the static Singleton
     * Factory method <code>View.getInstance()</code>
     *
     * @throws Error Error if Singleton instance has already been constructed
     */
    protected CoreView() {
        instance = this;

        this.mediatorMap = new HashMap<>();
        this.observerMap = new HashMap<>();
        initializeView();
    }

    /**
     * View Singleton Factory method.
     *
     * @return The Singleton instance of <code>View</code>
     */
    public synchronized static CoreView getInstance() {
        if (instance == null)
            instance = new CoreView();

        return instance;
    }

    /**
     * Initialize the Singleton View instance.
     * <p>
     * <p>
     * Called automatically by the constructor, this is your opportunity to
     * initialize the Singleton instance in your subclass without overriding
     * the constructor.
     * </P>
     */
    protected void initializeView() {
    }

    /**
     * Notify the <code>Observers</code> for a particular
     * <code>Notification</code>.
     * <p>
     * <p>
     * All previously attached <code>Observers</code> for this
     * <code>Notification</code>'s list are notified and are passed a
     * reference to the <code>Notification</code> in the order in which they
     * were registered.
     * </P>
     *
     * @param note the <code>Notification</code> to notify
     *             <code>Observers</code> of.
     */
    public void notifyObservers(Notification note) {
        List<Observer> observerList = observerMap.get(note.getName());
        if (observerList != null) {

            // Copy observers from reference array to working array,
            // since the reference array may change during the
            //notification loop
            Observer[] observers = observerList.toArray(new Observer[observerList.size()]);

            // Notify Observers from the working array
            for (Observer observer : observers) {
                observer.notifyObserver(note);
            }
        }
    }

    /**
     * Remove the observer for a given notifyContext from an observer list for a given Notification name.
     *
     * @param notificationName Which observer list to remove from
     * @param notifyContext    Remove the observer with this object as its notifyContext
     */
    public void removeObserver(String notificationName, Object notifyContext) {
        // the observer list for the notification under inspection
        List<Observer> observers = observerMap.get(notificationName);

        if (observers != null) {
            // find the observer for the notifyContext
            for (int i = 0; i < observers.size(); i++) {
                BaseObserver observer = (BaseObserver) observers.get(i);
                if (observer.compareNotifyContext(notifyContext)) {
                    observers.remove(observer);
                }
            }

            // Also, when a Notification's Observer list length falls to
            // zero, delete the notification key from the observer map
            if (observers.size() == 0) {
                observerMap.remove(notificationName);
            }
        }
    }

    /**
     * Register an <code>Mediator</code> instance with the <code>View</code>.
     * <p>
     * <P>
     * Registers the <code>Mediator</code> so that it can be retrieved by
     * name, and further interrogates the <code>Mediator</code> for its
     * <code>Notification</code> interests.
     * </P>
     * <P>
     * If the <code>Mediator</code> returns any <code>Notification</code>
     * names to be notified about, an <code>Observer</code> is created
     * encapsulating the <code>Mediator</code> instance's
     * <code>handleNotification</code> method and registering it as an
     * <code>Observer</code> for all <code>Notifications</code> the
     * <code>Mediator</code> is interested in.
     * </p>
     *
     * @param mediator the name to associate with this <code>IMediator</code>
     *                 instance
     */
    public void registerMediator(final Mediator mediator) {
        if (mediatorMap.containsKey(mediator.getMediatorName())) {
            return;
        }

        // Register the Mediator for retrieval by name
        mediatorMap.put(mediator.getMediatorName(), mediator);

        // Get Notification interests, if any.
        String[] noteInterests = mediator.listNotificationInterests();
        if (noteInterests.length != 0) {
            // Create Observer
            BaseObserver observer = new BaseObserver(mediator::handleNotification, mediator);

            // Register Mediator as Observer for its list of Notification
            // interests
            for (String noteInterest : noteInterests) {
                registerObserver(noteInterest, observer);
            }
        }

        // alert the mediator that it has been registered
        mediator.onRegister();
    }

    /**
     * Register an <code>Observer</code> to be notified of
     * <code>INotifications</code> with a given name.
     *
     * @param notificationName the name of the <code>Notifications</code> to notify this
     *                         <code>Observer</code> of
     * @param observer         the <code>Observer</code> to register
     */
    public void registerObserver(String notificationName, Observer observer) {
        if (observerMap.get(notificationName) == null) {
            observerMap.put(notificationName, new ArrayList<>());
        }

        List<Observer> observers = observerMap.get(notificationName);
        observers.add(observer);
    }

    /**
     * Remove an <code>Mediator</code> from the <code>View</code>.
     *
     * @param mediatorName name of the <code>Mediator</code> instance to be removed.
     */
    public Mediator removeMediator(String mediatorName) {
        // Retrieve the named mediator
        Mediator mediator = mediatorMap.get(mediatorName);

        if (mediator != null) {
            // for every notification this mediator is interested in...
            String[] interests = mediator.listNotificationInterests();
            for (String interest : interests) {
                // remove the observer linking the mediator
                // to the notification interest
                removeObserver(interest, mediator);
            }

            // remove the mediator from the map
            mediatorMap.remove(mediatorName);

            // alert the mediator that it has been removed
            mediator.onRemove();
        }

        return mediator;
    }

    /**
     * Retrieve an <code>Mediator</code> from the <code>View</code>.
     *
     * @param mediatorName the name of the <code>Mediator</code> instance to
     *                     retrieve.
     * @return the <code>Mediator</code> instance previously registered with
     * the given <code>mediatorName</code>.
     */
    public <T extends Mediator> T retrieveMediator(String mediatorName) {
        return (T) mediatorMap.get(mediatorName);
    }

    /**
     * Check if a Mediator is registered or not
     *
     * @param mediatorName
     * @return whether a Mediator is registered with the given <code>mediatorName</code>.
     */
    public boolean hasMediator(String mediatorName) {
        return mediatorMap.containsKey(mediatorName);
    }
}
