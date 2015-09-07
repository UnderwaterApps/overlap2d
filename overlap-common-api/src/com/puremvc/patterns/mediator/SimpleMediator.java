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
