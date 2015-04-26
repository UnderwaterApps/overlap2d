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


import com.puremvc.core.CoreController;
import com.puremvc.core.CoreModel;
import com.puremvc.core.CoreView;
import com.puremvc.patterns.command.Command;
import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.observer.BaseNotification;
import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.proxy.Proxy;

/**
 * A base Singleton <code>Facade</code> implementation.
 *
 * @see CoreModel Model
 * @see CoreView View
 * @see CoreController Controller
 */
public class SimpleFacade implements Facade {
    /**
     * The Singleton instance of the Facade
     */
    protected static SimpleFacade instance = null;

    /**
     * Reference to the Controller
     */
    protected CoreController controller = null;

    /**
     * Reference to the Model
     */
    protected CoreModel model = null;

    /**
     * Reference to the View
     */
    protected CoreView view = null;

    /**
     * Constructor.
     * <p>
     * <p>
     * This <code>IFacade</code> implementation is a Singleton, so you should
     * not call the constructor directly, but instead call the static Singleton
     * Factory method <code>Facade.getInstance()</code>
     */
    protected SimpleFacade() {
        initializeFacade();
    }

    /**
     * Facade Singleton Factory method
     *
     * @return The Singleton instance of the Facade
     */
    public synchronized static SimpleFacade getInstance() {
        if (instance == null) {
            instance = new SimpleFacade();
        }

        return instance;
    }

    /**
     * Initialize the Multiton <code>Facade</code> instance.
     * <p>
     * <p>
     * Called automatically by the constructor. Override in your
     * subclass to do any subclass specific initializations. Be
     * sure to call <code>super.initializeFacade()</code>, though.</P>
     */
    protected void initializeFacade() {
        initializeModel();
        initializeController();
        initializeView();
    }

    /**
     * Initialize the <code>Controller</code>.
     * <p>
     * <p>
     * Called by the <code>initializeFacade</code> method. Override this
     * method in your subclass of <code>Facade</code> if one or both of the
     * following are true:
     * <UL>
     * <LI> You wish to initialize a different <code>IController</code>.</LI>
     * <LI> You have <code>Commands</code> to register with the
     * <code>Controller</code> at startup.</code>. </LI>
     * </UL>
     * If you don't want to initialize a different <code>IController</code>,
     * call <code>super.initializeController()</code> at the beginning of your
     * method, then register <code>Command</code>s.
     * </P>
     */
    protected void initializeController() {
        if (controller != null) {
            return;
        }

        controller = CoreController.getInstance();
    }

    /**
     * Initialize the <code>Model</code>.
     * <p>
     * <p>
     * Called by the <code>initializeFacade</code> method. Override this
     * method in your subclass of <code>Facade</code> if one or both of the
     * following are true:
     * <UL>
     * <LI> You wish to initialize a different <code>IModel</code>.</LI>
     * <LI> You have <code>Proxy</code>s to register with the Model that do
     * not retrieve a reference to the Facade at construction time.</code></LI>
     * </UL>
     * If you don't want to initialize a different <code>IModel</code>, call
     * <code>super.initializeModel()</code> at the beginning of your method,
     * then register <code>Proxy</code>s.
     * <p>
     * Note: This method is <i>rarely</i> overridden; in practice you are more
     * likely to use a <code>Command</code> to create and register <code>Proxy</code>s
     * with the <code>Model</code>, since <code>Proxy</code>s with mutable
     * tools will likely need to send <code>INotification</code>s and thus
     * will likely want to fetch a reference to the <code>Facade</code> during
     * their construction.
     * </P>
     */
    protected void initializeModel() {
        if (model != null) {
            return;
        }

        model = CoreModel.getInstance();
    }

    /**
     * Initialize the <code>View</code>.
     * <p>
     * <p>
     * Called by the <code>initializeFacade</code> method. Override this
     * method in your subclass of <code>Facade</code> if one or both of the
     * following are true:
     * <UL>
     * <LI> You wish to initialize a different <code>IView</code>.</LI>
     * <LI> You have <code>Observers</code> to register with the
     * <code>View</code></LI>
     * </UL>
     * If you don't want to initialize a different <code>IView</code>, call
     * <code>super.initializeView()</code> at the beginning of your method,
     * then register <code>IMediator</code> instances.
     * <p>
     * Note: This method is <i>rarely</i> overridden; in practice you are more
     * likely to use a <code>Command</code> to create and register
     * <code>Mediator</code>s with the <code>View</code>, since
     * <code>IMediator</code> instances will need to send
     * <code>INotification</code>s and thus will likely want to fetch a
     * reference to the <code>Facade</code> during their construction.
     * </P>
     */
    protected void initializeView() {
        if (view != null) {
            return;
        }

        view = CoreView.getInstance();
    }

    /**
     * Register an <code>ICommand</code> with the <code>Controller</code> by
     * Notification name.
     *
     * @param noteName the name of the <code>INotification</code> to associate the
     *                 <code>ICommand</code> with
     * @param command  an instance of the <code>ICommand</code>
     */
    public void registerCommand(String noteName, Class<? extends Command> command) {
        controller.registerCommand(noteName, command);
    }

    /**
     * Remove a previously registered <code>ICommand</code> to <code>INotification</code> mapping from the Controller.
     *
     * @param notificationName the name of the <code>INotification</code> to remove the <code>ICommand</code> mapping for
     */
    public void removeCommand(String notificationName) {
        controller.removeCommand(notificationName);
    }

    /**
     * Check if a Command is registered for a given Notification
     *
     * @param notificationName
     * @return whether a Command is currently registered for the given <code>notificationName</code>.
     */
    public boolean hasCommand(String notificationName) {
        return controller.hasCommand(notificationName);
    }

    /**
     * Register a <code>IMediator</code> with the <code>View</code>.
     *
     * @param mediator the name to associate with this <code>IMediator</code>
     */
    public void registerMediator(Mediator mediator) {
        if (view != null) {
            view.registerMediator(mediator);
        }
    }

    /**
     * Register an <code>IProxy</code> with the <code>Model</code> by name.
     *
     * @param proxy the name of the <code>IProxy</code> instance to be
     *              registered with the <code>Model</code>.
     */
    public void registerProxy(Proxy proxy) {
        model.registerProxy(proxy);
    }



    /**
     * Remove an <code>IMediator</code> from the <code>View</code>.
     *
     * @param mediatorName name of the <code>IMediator</code> to be removed.
     * @return the <code>IMediator</code> that was removed from the <code>View</code>
     */
    public Mediator removeMediator(String mediatorName) {
        if (this.view != null) {
            return this.view.removeMediator(mediatorName);
        }
        return null;
    }

    /**
     * Remove an <code>IProxy</code> from the <code>Model</code> by name.
     *
     * @param proxyName the <code>IProxy</code> to remove from the
     *                  <code>Model</code>.
     * @return the <code>IProxy</code> that was removed from the <code>Model</code>
     */
    public Proxy removeProxy(String proxyName) {
        if (model != null) {
            return model.removeProxy(proxyName);
        }
        return null;
    }

    /**
     * Check if a Proxy is registered
     *
     * @param proxyName
     * @return whether a Proxy is currently registered with the given <code>proxyName</code>.
     */
    public boolean hasProxy(String proxyName) {
        return model.hasProxy(proxyName);
    }


    /**
     * Check if a Mediator is registered or not
     *
     * @param mediatorName
     * @return whether a Mediator is registered with the given <code>mediatorName</code>.
     */
    public boolean hasMediator(String mediatorName) {
        return view.hasMediator(mediatorName);
    }

    /**
     * Retrieve an <code>IMediator</code> from the <code>View</code>.
     *
     * @param mediatorName
     * @return the <code>IMediator</code> previously registered with the given
     * <code>mediatorName</code>.
     */
    public <T extends Mediator> T retrieveMediator(String mediatorName) {
        return this.view.retrieveMediator(mediatorName);
    }

    /**
     * Retrieve an <code>IProxy</code> from the <code>Model</code> by name.
     *
     * @param proxyName the name of the proxy to be retrieved.
     * @return the <code>IProxy</code> instance previously registered with the
     * given <code>proxyName</code>.
     */

    @Override
    public <T extends Proxy> T retrieveProxy(String proxyName) {
         return this.model.retrieveProxy(proxyName);
    }
    /**
     * Create and send an <code>INotification</code>.
     * <p>
     * <p>
     * Keeps us from having to construct new notification
     * instances in our implementation code.
     *
     * @param notificationName the name of the notification to send
     * @param body             the body of the notification (optional)
     * @param type             the type of the notification (optional)
     */
    public void sendNotification(String notificationName, Object body, String type) {
        notifyObservers(new BaseNotification(notificationName, body, type));
    }

    /**
     * Create and send an <code>INotification</code>.
     * <p>
     * <p>
     * Keeps us from having to construct new notification
     * instances in our implementation code.
     *
     * @param notificationName the name of the notification to send
     * @param body             the body of the notification (optional)
     */
    public void sendNotification(String notificationName, Object body) {
        sendNotification(notificationName, body, null);
    }

    /**
     * Create and send an <code>INotification</code>.
     * <p>
     * <p>
     * Keeps us from having to construct new notification
     * instances in our implementation code.
     *
     * @param notificationName the name of the notification to send
     */
    public void sendNotification(String notificationName) {
        sendNotification(notificationName, null, null);
    }

    /**
     * Notify <code>Observer</code>s of an <code>INotification</code>.
     *
     * @param note the <code>INotification</code> to have the <code>View</code>
     *             notify observers of.
     */
    public void notifyObservers(Notification note) {
        if (view != null) {
            view.notifyObservers(note);
        }
    }
}
