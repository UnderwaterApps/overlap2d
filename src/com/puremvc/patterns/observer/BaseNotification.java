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
package com.puremvc.patterns.observer;


/**
 * A base <code>Notification</code> implementation.
 * <p>
 * <p>
 * PureMVC does not rely upon underlying event models such as the one provided
 * with Flash, and ActionScript 3 does not have an inherent event model.
 * </P>
 * <p>
 * <p>
 * The Observer Pattern as implemented within PureMVC exists to support
 * event-driven communication between the application and the actors of the MVC
 * triad.
 * </P>
 * <p>
 * <p>
 * Notifications are not meant to be a replacement for Events in
 * Flex/Flash/Apollo. Generally, <code>IMediator</code> implementors place
 * event listeners on their view components, which they then handle in the usual
 * way. This may lead to the broadcast of <code>Notification</code>s to
 * trigger <code>ICommand</code>s or to communicate with other
 * <code>IMediators</code>. <code>IProxy</code> and <code>ICommand</code>
 * instances communicate with each other and <code>IMediator</code>s by
 * broadcasting <code>INotification</code>s.
 * </P>
 * <p>
 * <p>
 * A key difference between Flash <code>Event</code>s and PureMVC
 * <code>Notification</code>s is that <code>Event</code>s follow the
 * 'Chain of Responsibility' pattern, 'bubbling' up the display hierarchy until
 * some parent component handles the <code>Event</code>, while PureMVC
 * <code>Notification</code>s follow a 'Publish/Subscribe' pattern. PureMVC
 * classes need not be related to each other in a parent/child relationship in
 * order to communicate with one another using <code>Notification</code>s.
 *
 * @see com.puremvc.patterns.observer Observer
 */
public class BaseNotification implements Notification {

    // the name of the notification instance
    // the type of the notification instance
    protected String name = null;
    protected String type = null;

    // the body of the notification instance
    protected Object body = null;

    /**
     * Constructor.
     *
     * @param name name of the <code>Notification</code> instance. (required)
     * @param body the <code>Notification</code> body. (optional)
     * @param type the type of the <code>Notification</code> (optional)
     */
    public BaseNotification(String name, Object body, String type) {
        this.name = name;
        this.body = body;
        this.type = type;
    }

    /**
     * Constructor.
     *
     * @param name name of the <code>Notification</code> instance. (required)
     */
    public BaseNotification(String name) {
        this.name = name;
        body = null;
        type = null;
    }

    /**
     * Constructor.
     *
     * @param name name of the <code>Notification</code> instance. (required)
     * @param body the <code>Notification</code> body. (optional)
     */
    public BaseNotification(String name, Object body) {
        this.name = name;
        this.body = body;
        type = null;
    }

    /**
     * Get the body of the <code>Notification</code> instance.
     *
     * @return the body object.
     */
    public <T> T getBody() {
        return (T) body;
    }

    /**
     * Set the body of the <code>Notification</code> instance.
     *
     * @param body
     */
    public <T> void setBody(T body) {
        this.body = body;
    }

    /**
     * Get the name of the <code>Notification</code> instance.
     *
     * @return the name of the <code>Notification</code> instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type of the <code>Notification</code> instance.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the <code>Notification</code> instance.
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the string representation of the <code>Notification</code>
     * instance.
     *
     * @return the string representation of the <code>Notification</code>
     * instance.
     */
    public String toString() {
        String result = "Notification Name: " + getName() + " Body:";
        if (body != null)
            result += body.toString() + " Type:";
        else
            result += "null Type:";

        if (type != null)
            result += type;
        else
            result += "null ";

        return result;
    }
}
