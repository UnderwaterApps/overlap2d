/*
 PureMVC Java port by Frederic Saunier <frederic.saunier@puremvc.org>
 
 Adapted from sources of thoses different authors :
 	Donald Stinchfield <donald.stinchfield@puremvc.org>, et all
 	Ima OpenSource <opensource@ima.eu>
 	Anthony Quinault <anthony.quinault@puremvc.org>
 
 PureMVC - Copyright(c) 2006-10 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License
*/
package com.puremvc.patterns.proxy;

import com.puremvc.patterns.observer.Notifier;

/**
 * The interface definition for a PureMVC Proxy.
 * <p>
 * <p>
 * In PureMVC, <code>Iroxy</code> implementors assume these responsibilities:
 * </P>
 * <UL>
 * <LI>Implement a common method which returns the name of the Proxy.</LI>
 * </UL>
 * <p>
 * Additionally, <code>IProxy</code>s typically:
 * </P>
 * <UL>
 * <LI>Maintain references to one or more pieces of model data.</LI>
 * <LI>Provide methods for manipulating that data.</LI>
 * <LI>Generate <code>INotifications</code> when their model data changes.</LI>
 * <LI>Expose their name as a <code>public static const</code> called <code>NAME</code>, if they are not instantiated multiple times.</LI>
 * <LI>Encapsulate interaction with local or remote services used to fetch and
 * persist model data.</LI>
 * </UL>
 */
public interface Proxy extends Notifier {

    /**
     * Get the Proxy name
     *
     * @return the Proxy instance name
     */
    String getProxyName();

    /**
     * Get the data object
     *
     * @return the data as type Object
     */
    Object getData();

    /**
     * Set the data object
     *
     * @param data the data object
     */
    void setData(Object data);

    /**
     * Called by the Model when the Proxy is registered
     */
    void onRegister();

    /**
     * Called by the Model when the Proxy is removed
     */
    void onRemove();

}
