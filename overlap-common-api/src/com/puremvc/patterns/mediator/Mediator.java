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

import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Notifier;

/**
 * The interface definition for a PureMVC Mediator.
 * <p>
 * <p>
 * In PureMVC, <code>IMediator</code> implementors assume these
 * responsibilities:
 * </P>
 * <UL>
 * <LI>Implement a common method which returns a list of all
 * <code>INotification</code>s the <code>IMediator</code> has interest in.</LI>
 * <LI>Implement a common notification (callback) method.</LI>
 * </UL>
 * <p>
 * Additionally, <code>IMediator</code>s typically:
 * <UL>
 * <LI>Act as an intermediary between one or more view components such as text
 * panels or list controls, maintaining references and coordinating their
 * behavior.</LI>
 * <LI>In Flash-based apps, this is often the place where event listeners are
 * added to view components, and their handlers implemented.</LI>
 * <LI>Respond to and generate <code>INotifications</code>, interacting with
 * of the rest of the PureMVC app.
 * </UL>
 * </P>
 * <p>
 * When an <code>IMediator</code> is registered with the <code>IView</code>,
 * the <code>IView</code> will call the <code>IMediator</code>'s
 * <code>listNotificationInterests</code> method. The <code>IMediator</code>
 * will return an <code>Array</code> of <code>INotification</code> names
 * which it wishes to be notified about.
 * </P>
 * <p>
 * <p>
 * The <code>IView</code> will then create an <code>Observer</code> object
 * encapsulating that <code>IMediator</code>'s (<code>handleNotification</code>)
 * method and register it as an Observer for each <code>INotification</code>
 * name returned by <code>listNotificationInterests</code>.
 * </P>
 * <p>
 * <p>
 * A concrete IMediator implementor usually looks something like this:
 * </P>
 * <p>
 * <listing> import org.puremvc.patterns.mediator.~~; import
 * org.puremvc.patterns.observer.~~; import org.puremvc.core.view.~~;
 * <p>
 * import com.me.myapp.model.~~; import com.me.myapp.view.~~; import
 * com.me.myapp.controller.~~;
 * <p>
 * import mx.controls.ComboBox; import mx.events.ListEvent;
 * <p>
 * public class MyMediator extends Mediator implements IMediator {
 * <p>
 * public function MyComboMediator( viewComponent:Object ) { super(
 * viewComponent ); combo.addEventListener( Event.CHANGE, onChange ); }
 * <p>
 * public function listNotificationInterests():Array { return [
 * MyFacade.SET_SELECTION, MyFacade.SET_DATAPROVIDER ]; }
 * <p>
 * public function handleNotification( notification:INotification ):void {
 * switch ( notification.getName() ) { case MyFacade.SET_SELECTION:
 * setSelection(notification); break; case MyFacade.SET_DATAPROVIDER:
 * setDataProvider(notification); break; } } // Set the tools provider of the
 * combo box private function setDataProvider( notification:INotification ):void {
 * combo.dataProvider = notification.getBody() as Array; } // Invoked when the
 * combo box dispatches a change event, we send a // notification with the
 * private function onChange(event:ListEvent):void { sendNotification(
 * MyFacade.MYCOMBO_CHANGED, this ); } // A private getter for accessing the
 * view object by class private function get combo():ComboBox { return view as
 * ComboBox; } } </listing>
 *
 * @see com.puremvc.patterns.observer.Notification Notification
 */
public interface Mediator<V> extends Notifier {

    /**
     * Get the <code>IMediator</code> instance name
     *
     * @return the <code>IMediator</code> instance name
     */
    String getMediatorName();

    /**
     * Get the <code>IMediator</code>'s view component.
     *
     * @return Object the view component
     */
    V getViewComponent();

    /**
     * Set the <code>IMediator</code>'s view component.
     *
     * @param viewComponent The view component
     */
    void setViewComponent(V viewComponent);

    /**
     * List <code>INotification</code> interests.
     *
     * @return an <code>Array</code> of the <code>INotification</code> names
     * this <code>IMediator</code> has an interest in.
     */
    String[] listNotificationInterests();

    /**
     * Handle an <code>INotification</code>.
     *
     * @param notification the <code>INotification</code> to be handled
     */
    void handleNotification(Notification notification);

    /**
     * Called by the View when the Mediator is registered
     */
    void onRegister();

    /**
     * Called by the View when the Mediator is removed
     */
    void onRemove();
}
