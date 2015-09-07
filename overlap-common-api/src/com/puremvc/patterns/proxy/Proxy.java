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
 * <LI>Maintain references to one or more pieces of model tools.</LI>
 * <LI>Provide methods for manipulating that tools.</LI>
 * <LI>Generate <code>INotifications</code> when their model tools changes.</LI>
 * <LI>Expose their name as a <code>public static const</code> called <code>NAME</code>, if they are not instantiated multiple times.</LI>
 * <LI>Encapsulate interaction with local or remote services used to fetch and
 * persist model tools.</LI>
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
     * Get the tools object
     *
     * @return the tools as type Object
     */
    Object getData();

    /**
     * Set the tools object
     *
     * @param data the tools object
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
