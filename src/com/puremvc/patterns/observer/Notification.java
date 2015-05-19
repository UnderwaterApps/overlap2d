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
 * The interface definition for a PureMVC Notification.
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
 * @see com.puremvc.core.View View
 * @see com.puremvc.patterns.observer.Observer Observer
 */
public interface Notification {

    /**
     * Get the name of the <code>INotification</code> instance. No setter,
     * should be set by constructor only
     *
     * @return the name
     */
    String getName();

    /**
     * Get the body of the <code>INotification</code> instance
     *
     * @return the body
     */
    <T> T getBody();

    /**
     * Set the body of the <code>INotification</code> instance
     *
     * @param body
     */
    <T> void setBody(T body);

    /**
     * Get the type of the <code>INotification</code> instance
     *
     * @return the type
     */
    String getType();

    /**
     * Set the type of the <code>Notification</code> instance
     *
     * @param type the type
     */
    void setType(String type);

    /**
     * Get the string representation of the <code>INotification</code>
     * instance
     *
     * @return the string representation of the <code>INotification</code>
     * instance
     */
    String toString();
}
