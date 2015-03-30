/*
 PureMVC Java port by Frederic Saunier <frederic.saunier@puremvc.org>
 
 Adapted from sources of thoses different authors :
 	Donald Stinchfield <donald.stinchfield@puremvc.org>, et all
 	Ima OpenSource <opensource@ima.eu>
 	Anthony Quinault <anthony.quinault@puremvc.org>
 
 PureMVC - Copyright(c) 2006-10 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License
*/
package com.puremvc.patterns.mediator;


import com.puremvc.patterns.observer.BaseNotifier;
import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Notifier;

/**
 * A base <code>Mediator</code> implementation.
 *
 * @see com.puremvc.core.View View
 */
public class SimpleMediator<V> extends BaseNotifier implements Mediator<V>, Notifier {

    /**
     * The default name of the <code>SimpleMediator</code>.
     */
    public static final String NAME = "SimpleMediator";

    /**
     * The name of the <code>Mediator</code>.
     */
    protected String mediatorName = null;

    /**
     * The view component
     */
    protected V viewComponent = null;

    /**
     * Constructor.
     *
     * @param mediatorName
     * @param viewComponent
     */
    public SimpleMediator(String mediatorName, V viewComponent) {
        this.mediatorName = (mediatorName != null) ? mediatorName : NAME;
        this.viewComponent = viewComponent;
    }

    /**
     * Get the name of the <code>Mediator</code>.
     *
     * @return the name
     */
    public final String getMediatorName() {
        return mediatorName;
    }

    /**
     * Get the <code>Mediator</code>'s view component.
     * <p>
     * <p>
     * Additionally, an implicit getter will usually be defined in the subclass
     * that casts the view object to a type, like this:
     * </P>
     * <p>
     * <listing> private function get comboBox : mx.controls.ComboBox { return
     * viewComponent as mx.controls.ComboBox; } </listing>
     *
     * @return the view component
     */
    public V getViewComponent() {
        return viewComponent;
    }

    /**
     * Set the <code>IMediator</code>'s view component.
     *
     * @param viewComponent The view component
     */
    public void setViewComponent(V viewComponent) {
        this.viewComponent = viewComponent;
    }

    /**
     * Handle <code>INotification</code>s.
     * <p>
     * <p>
     * Typically this will be handled in a switch statement, with one 'case'
     * entry per <code>INotification</code> the <code>Mediator</code> is
     * interested in.
     *
     * @param notification
     */
    public void handleNotification(Notification notification) {
    }

    /**
     * List the <code>INotification</code> names this <code>Mediator</code>
     * is interested in being notified of.
     *
     * @return String[] the list of <code>INotification</code> names
     */
    public String[] listNotificationInterests() {
        return new String[]{};
    }

    /**
     * Called by the View when the Mediator is registered
     */
    public void onRegister() {
    }

    /**
     * Called by the View when the Mediator is removed
     */
    public void onRemove() {
    }
}
