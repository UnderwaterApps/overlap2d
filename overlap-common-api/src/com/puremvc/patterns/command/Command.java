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
