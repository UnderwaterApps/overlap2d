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

import com.puremvc.patterns.command.Command;
import com.puremvc.patterns.observer.Notification;

/**
 * The interface definition for a PureMVC Controller.
 * <p>
 * <p>
 * In PureMVC, an <code>IController</code> implementor follows the 'Command
 * and Controller' strategy, and assumes these responsibilities:
 * <UL>
 * <LI> Remembering which <code>ICommand</code>s are intended to handle which
 * <code>INotifications</code>.</LI>
 * <LI> Registering itself as an <code>IObserver</code> with the
 * <code>View</code> for each <code>INotification</code> that it has an
 * <code>ICommand</code> mapping for.</LI>
 * <LI> Creating a new instance of the proper <code>ICommand</code> to handle
 * a given <code>INotification</code> when notified by the <code>View</code>.</LI>
 * <LI> Calling the <code>ICommand</code>'s <code>execute</code> method,
 * passing in the <code>INotification</code>.</LI>
 * </UL>
 *
 * @see com.puremvc.patterns.observer Notification
 * @see com.puremvc.patterns.command Command
 */
public interface Controller {

    /**
     * Register a particular <code>ICommand</code> class as the handler for a
     * particular <code>INotification</code>.
     *
     * @param notificationName the name of the <code>INotification</code>
     * @param command          the Class of the <code>ICommand</code>
     */
    void registerCommand(String notificationName, Class<? extends Command>  command);

    /**
     * Execute the <code>ICommand</code> previously registered as the handler
     * for <code>INotification</code>s with the given notification name.
     *
     * @param notification the <code>INotification</code> to execute the associated
     *                     <code>ICommand</code> for
     */
    void executeCommand(Notification notification);

    /**
     * Remove a previously registered <code>ICommand</code> to
     * <code>INotification</code> mapping.
     *
     * @param notificationName the name of the <code>INotification</code> to remove the
     *                         <code>ICommand</code> mapping for
     */
    void removeCommand(String notificationName);

    /**
     * Check if a Command is registered for a given Notification
     *
     * @param notificationName
     * @return whether a Command is currently registered for the given <code>notificationName</code>.
     */
    boolean hasCommand(String notificationName);
}
