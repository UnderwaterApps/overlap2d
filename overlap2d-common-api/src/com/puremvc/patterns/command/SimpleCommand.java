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
