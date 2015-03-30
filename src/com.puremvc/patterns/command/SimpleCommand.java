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


import com.puremvc.patterns.observer.BaseNotifier;
import com.puremvc.patterns.observer.Notification;

/**
 * A base <code>ICommand</code> implementation.
 * <p>
 * <p>
 * Your subclass should override the <code>execute</code> method where your
 * business logic will handle the <code>INotification</code>.
 * </P>
 *
 * @see com.puremvc.core.Controller Controller
 * @see com.puremvc.patterns.observer.Notification Notification
 * @see com.puremvc.patterns.command.MacroCommand MacroCommand
 */
public class SimpleCommand extends BaseNotifier implements Command {

    /**
     * Fulfill the use-case initiated by the given <code>INotification</code>.
     * <p>
     * <p>
     * In the Command Pattern, an application use-case typically begins with
     * some user action, which results in an <code>INotification</code> being
     * broadcast, which is handled by business logic in the <code>execute</code>
     * method of an <code>ICommand</code>.
     * </P>
     *
     * @param notification the <code>INotification</code> to handle.
     */
    public void execute(Notification notification) {
    }

}
