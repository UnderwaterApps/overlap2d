/*
 PureMVC Java port by Frederic Saunier <frederic.saunier@puremvc.org>
 
 Adapted from sources of thoses different authors :
 	Donald Stinchfield <donald.stinchfield@puremvc.org>, et all
 	Ima OpenSource <opensource@ima.eu>
 	Anthony Quinault <anthony.quinault@puremvc.org>
 
 PureMVC - Copyright(c) 2006-10 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License
*/
package com.puremvc.patterns.command;

import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Notifier;

/**
 * The interface definition for a PureMVC Command.
 *
 * @see com.puremvc.patterns.observer Notification
 */
public interface Command extends Notifier {

    /**
     * Execute the <code>Command</code>'s logic to handle a given
     * <code>Notification</code>.
     *
     * @param notification an <code>Notification</code> to handle.
     */
    void execute(Notification notification);
}
