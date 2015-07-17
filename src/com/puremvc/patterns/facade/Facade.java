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
package com.puremvc.patterns.facade;

import com.puremvc.patterns.command.Command;
import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Notifier;
import com.puremvc.patterns.proxy.Proxy;

/**
 * The interface definition for a PureMVC Facade.
 * <p>
 * <p>
 * The Facade Pattern suggests providing a single class to act as a central
 * point of communication for a subsystem.
 * </P>
 * <p>
 * <p>
 * In PureMVC, the Facade acts as an interface between the core MVC actors
 * (Model, View, Controller) and the rest of your application.
 * </P>
 *
 * @see com.puremvc.core.Model Model
 * @see com.puremvc.core.View View
 * @see com.puremvc.core.Controller Controller
 * @see com.puremvc.patterns.command.Command Command
 * @see com.puremvc.patterns.observer.Notification Notification
 */
public interface Facade extends Notifier {
    /**
     * Notify <code>Observer</code>s of an <code>INotification</code>.
     *
     * @param note the <code>INotification</code> to have the <code>View</code>
     *             notify observers of.
     */
    void notifyObservers(Notification note);

    /**
     * Register an <code>IProxy</code> with the <code>Model</code> by name.
     *
     * @param proxy the <code>IProxy</code> to be registered with the
     *              <code>Model</code>.
     */
    void registerProxy(Proxy proxy);

    /**
     * Retrieve a <code>IProxy</code> from the <code>Model</code> by name.
     *
     * @param proxyName the name of the <code>IProxy</code> instance to be
     *                  retrieved.
     * @return the <code>IProxy</code> previously regisetered by
     * <code>proxyName</code> with the <code>Model</code>.
     */
    <T extends Proxy> T retrieveProxy(String proxyName);

    /**
     * Remove an <code>IProxy</code> instance from the <code>Model</code> by
     * name.
     *
     * @param proxyName the <code>IProxy</code> to remove from the
     *                  <code>Model</code>.
     */
    <T extends Proxy> T removeProxy(String proxyName);

    /**
     * Check if a Proxy is registered
     *
     * @param proxyName
     * @return whether a Proxy is currently registered with the given <code>proxyName</code>.
     */
    boolean hasProxy(String proxyName);

    /**
     * Register an <code>ICommand</code> with the <code>Controller</code>.
     *
     * @param noteName        the name of the <code>INotification</code> to associate the
     *                        <code>ICommand</code> with.
     * @param commandClassRef a reference to the <code>Class</code> of the
     *                        <code>ICommand</code>.
     */
    void registerCommand(String noteName, Class<? extends Command>  commandClassRef);

    /**
     * Remove a previously registered <code>ICommand</code> to <code>INotification</code> mapping from the Controller.
     *
     * @param notificationName the name of the <code>INotification</code> to remove the <code>ICommand</code> mapping for
     */
    void removeCommand(String notificationName);

    /**
     * Check if a Command is registered for a given Notification
     *
     * @param notificationName
     * @return whether a Command is currently registered for the given <code>notificationName</code>.
     */
    boolean hasCommand(String notificationName);

    /**
     * Register an <code>IMediator</code> instance with the <code>View</code>.
     *
     * @param mediator a reference to the <code>IMediator</code> instance
     */
    void registerMediator(Mediator mediator);

    /**
     * Retrieve an <code>IMediator</code> instance from the <code>View</code>.
     *
     * @param mediatorName the name of the <code>IMediator</code> instance to retrievve
     * @return the <code>IMediator</code> previously registered with the given
     * <code>mediatorName</code>.
     */
    <T extends Mediator> T retrieveMediator(String mediatorName);

    /**
     * Check if a Mediator is registered or not
     *
     * @param mediatorName
     * @return whether a Mediator is registered with the given <code>mediatorName</code>.
     */
    boolean hasMediator(String mediatorName);

    /**
     * Remove a <code>IMediator</code> instance from the <code>View</code>.
     *
     * @param mediatorName name of the <code>IMediator</code> instance to be removed.
     */
    Mediator removeMediator(String mediatorName);
}
