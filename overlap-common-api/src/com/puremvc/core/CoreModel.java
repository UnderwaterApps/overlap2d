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

import com.puremvc.patterns.proxy.Proxy;

/**
 * A Singleton <code>IModel</code> implementation.
 * <p>
 * <P>
 * In PureMVC, the <code>Model</code> class provides
 * access to model objects (Proxies) by named lookup.
 * <p>
 * <P>
 * The <code>Model</code> assumes these responsibilities:</P>
 * <p>
 * <UL>
 * <LI>Maintain a cache of <code>IProxy</code> instances.</LI>
 * <LI>Provide methods for registering, retrieving, and removing
 * <code>IProxy</code> instances.</LI>
 * </UL>
 * <p>
 * <P>
 * Your application must register <code>IProxy</code> instances
 * with the <code>Model</code>. Typically, you use an
 * <code>ICommand</code> to create and register <code>IProxy</code>
 * instances once the <code>Facade</code> has initialized the Core
 * actors.</p>
 *
 * @see com.puremvc.patterns.proxy.BaseProxy BaseProxy
 * @see com.puremvc.patterns.proxy.Proxy Proxy
 */
public class CoreModel implements Model {

    /**
     * Singleton instance
     */
    protected static CoreModel instance;

    /**
     * Mapping of proxyNames to IProxy instances
     */
    protected Map<String, Proxy> proxyMap;

    /**
     * Constructor.
     * <p>
     * <p>
     * This <code>IModel</code> implementation is a Multiton,
     * so you should not call the constructor
     * directly, but instead call the static Multiton
     * Factory method <code>Model.getInstance( multitonKey )</code>
     *
     * @throws Error Error if instance for this Multiton key instance has already been constructed
     */
    protected CoreModel() {
        instance = this;
        proxyMap = new HashMap<>();
        initializeModel();
    }

    /**
     * <code>Model</code> Multiton Factory method.
     *
     * @return the instance for this Multiton key
     */
    public synchronized static CoreModel getInstance() {
        if (instance == null) {
            instance = new CoreModel();
        }

        return instance;
    }

    /**
     * Initialize the Singleton <code>Model</code> instance.
     * <p>
     * <p>
     * Called automatically by the constructor, this is your opportunity to
     * initialize the Singleton instance in your subclass without overriding the
     * constructor.
     * </P>
     */
    protected void initializeModel() {
    }

    /**
     * Register an <code>Proxy</code> with the <code>Model</code>.
     *
     * @param proxy an <code>Proxy</code> to be held by the <code>Model</code>.
     */
    public void registerProxy(Proxy proxy) {
        proxyMap.put(proxy.getProxyName(), proxy);
        proxy.onRegister();
    }

    /**
     * Remove an <code>Proxy</code> from the <code>Model</code>.
     *
     * @param proxyName Name of the <code>Proxy</code> instance to be removed.
     * @return The <code>IProxy</code> that was removed from the <code>Model</code>
     */
    public Proxy removeProxy(String proxyName) {
        Proxy proxy = proxyMap.get(proxyName);

        if (proxy != null) {
            proxyMap.remove(proxyName);
            proxy.onRemove();
        }

        return proxy;
    }

    /**
     * Retrieve an <code>Proxy</code> from the <code>Model</code>.
     *
     * @param proxy
     * @return the <code>Proxy</code> instance previously registered with the
     * given <code>proxyName</code>.
     */
    public <T extends Proxy>  T retrieveProxy(String proxy) {
        return (T) proxyMap.get(proxy);
    }

    /**
     * Check if a Proxy is registered
     *
     * @param proxyName Name of the <code>Proxy</code> object to check for existance.
     * @return Whether a Proxy is currently registered with the given <code>proxyName</code>.
     */
    public boolean hasProxy(String proxyName) {
        return proxyMap.containsKey(proxyName);
    }
}
