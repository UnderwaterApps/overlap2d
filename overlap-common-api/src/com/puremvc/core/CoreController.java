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


import java.util.HashMap;
import java.util.Map;

import com.puremvc.patterns.command.Command;
import com.puremvc.patterns.observer.BaseObserver;
import com.puremvc.patterns.observer.Notification;

/**
 * A Singleton <code>Controller</code> implementation.
 * <p>
 * <p>
 * In PureMVC, the <code>Controller</code> class follows the
 * 'Command and Controller' strategy, and assumes these
 * responsibilities:
 * <UL>
 * <LI> Remembering which <code>ICommand</code>s
 * are intended to handle which <code>INotifications</code>.</LI>
 * <LI> Registering itself as an <code>IObserver</code> with
 * the <code>View</code> for each <code>INotification</code>
 * that it has an <code>ICommand</code> mapping for.</LI>
 * <LI> Creating a new instance of the proper <code>ICommand</code>
 * to handle a given <code>INotification</code> when notified by the <code>View</code>.</LI>
 * <LI> Calling the <code>ICommand</code>'s <code>execute</code>
 * method, passing in the <code>INotification</code>.</LI>
 * </UL>
 * <p>
 * <p>
 * Your application must register <code>ICommands</code> with the
 * Controller.
 * <p>
 * The simplest way is to subclass </code>Facade</code>,
 * and use its <code>initializeController</code> method to add your
 * registrations.
 *
 * @see CoreView View
 * @see BaseObserver Observer
 * @see com.puremvc.patterns.observer.Notification Notification
 * @see com.puremvc.patterns.command.SimpleCommand SimpleCommand
 * @see com.puremvc.patterns.command.MacroCommand MacroCommand
 */
public class CoreController implements Controller {

    /**
     * Reference to the singleton instance
     */
    protected static CoreController instance;
    /**
     * Mapping of Notification names to Command Class references
     */
    protected Map<String, Class<? extends Command>> commandMap;

    /**
     * Local reference to View
     */
    protected CoreView view;

    /**
     * Constructor.
     * <p>
     * <p>
     * This <code>IController</code> implementation is a Singleton, so you
     * should not call the constructor directly, but instead call the static
     * Singleton Factory method <code>Controller.getInstance()</code>
     */
    protected CoreController() {
        instance = this;
        commandMap = new HashMap<>();
        initializeController();
    }

    /**
     * <code>Controller</code> Singleton Factory method.
     *
     * @return the Singleton instance of <code>Controller</code>
     */
    public synchronized static CoreController getInstance() {
        if (instance == null) {
            instance = new CoreController();
        }

        return instance;
    }

    /**
     * Initialize the Singleton <code>Controller</code> instance.
     * <p>
     * <P>Called automatically by the constructor.</P>
     * <p>
     * <P>Note that if you are using a subclass of <code>View</code>
     * in your application, you should <i>also</i> subclass <code>Controller</code>
     * and override the <code>initializeController</code> method in the
     * following way:</P>
     * <p>
     * <listing>
     * // ensure that the Controller is talking to my IView implementation
     * override public function initializeController(  ) : void
     * {
     * view = MyView.getInstance();
     * }
     * </listing>
     */
    protected void initializeController() {
        view = CoreView.getInstance();
    }

    /**
     * If an <code>ICommand</code> has previously been registered to handle a
     * the given <code>INotification</code>, then it is executed.
     *
     * @param note The notification to send associated with the command to call.
     */
    public void executeCommand(Notification note) {
        //No reflexion in GWT
        Class<? extends Command> commandClass = commandMap.get(note.getName());
        if (commandClass != null) {
            Command command;
            try {
                command = commandClass.newInstance();
                command.execute(note);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Register a particular <code>ICommand</code> class as the handler for a
     * particular <code>INotification</code>.
     * <p>
     * <p>
     * If an <code>ICommand</code> has already been registered to handle
     * <code>INotification</code>s with this name, it is no longer used, the
     * new <code>ICommand</code> is used instead.
     * </P>
     * <p>
     * The Observer for the new ICommand is only created if this the
     * first time an ICommand has been regisered for this Notification name.
     *
     * @param notificationName the name of the <code>Notification</code>
     * @param command          an instance of <code>Command</code>
     */
    public void registerCommand(String notificationName, Class<? extends Command> command) {
        if (null != commandMap.put(notificationName, command)) {
            return;
        }

        view.registerObserver
                (
                        notificationName,
                        new BaseObserver(this::executeCommand, this)
                );
    }

    /**
     * Remove a previously registered <code>ICommand</code> to
     * <code>INotification</code> mapping.
     *
     * @param notificationName the name of the <code>INotification</code> to remove the
     *                         <code>ICommand</code> mapping for
     */
    public void removeCommand(String notificationName) {
        // if the Command is registered...
        if (hasCommand(notificationName)) {
            // remove the observer
            view.removeObserver(notificationName, this);
            commandMap.remove(notificationName);
        }
    }

    /**
     * Check if a Command is registered for a given Notification
     *
     * @param notificationName The name of the command to check for existance.
     * @return whether a Command is currently registered for the given <code>notificationName</code>.
     */
    public boolean hasCommand(String notificationName) {
        return commandMap.containsKey(notificationName);
    }
}
