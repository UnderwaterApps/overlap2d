/*
 PureMVC Java port by Frederic Saunier <frederic.saunier@puremvc.org>
 
 Adapted from sources of thoses different authors :
 	Donald Stinchfield <donald.stinchfield@puremvc.org>, et all
 	Ima OpenSource <opensource@ima.eu>
 	Anthony Quinault <anthony.quinault@puremvc.org>
 
 PureMVC - Copyright(c) 2006-10 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License
*/
package com.puremvc.core;

import com.puremvc.patterns.proxy.Proxy;

/**
 * The interface definition for a PureMVC Model.
 * <p>
 * <p>
 * In PureMVC, <code>Model</code> implementors provide access to
 * <code>Proxy</code> objects by named lookup.
 * </P>
 * <p>
 * <p>
 * An <code>Model</code> assumes these responsibilities:
 * </P>
 * <p>
 * <UL>
 * <LI>Maintain a cache of <code>Proxy</code> instances</LI>
 * <LI>Provide methods for registering, retrieving, and removing
 * <code>Proxy</code> instances</LI>
 * </UL>
 */
public interface Model {

    /**
     * Register an <code>Proxy</code> instance with the <code>Model</code>.
     *
     * @param proxy an object reference to be held by the <code>Model</code>.
     */
    void registerProxy(Proxy proxy);

    /**
     * Retrieve an <code>Proxy</code> instance from the Model.
     *
     * @param proxy
     * @return the <code>Proxy</code> instance previously registered with the
     * given <code>proxyName</code>.
     */
    <T extends Proxy> T retrieveProxy(String proxy);

    /**
     * Remove an <code>Proxy</code> instance from the Model.
     *
     * @param proxy name of the <code>Proxy</code> instance to be removed.
     */
    Proxy removeProxy(String proxy);

    /**
     * Check if a Proxy is registered
     *
     * @param proxyName
     * @return whether a Proxy is currently registered with the given <code>proxyName</code>.
     */
    boolean hasProxy(String proxyName);
}
