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


import java.util.Collection;
import java.util.Vector;

import com.puremvc.patterns.observer.BaseNotifier;
import com.puremvc.patterns.observer.Notification;


/**
 * A base <code>ICommand</code> implementation that executes other
 * <code>ICommand</code>s.
 * <p>
 * <p>
 * A <code>MacroCommand</code> maintains an list of <code>ICommand</code>
 * Class references called <i>SubCommands</i>.
 * </P>
 * <p>
 * <p>
 * When <code>execute</code> is called, the <code>MacroCommand</code>
 * instantiates and calls <code>execute</code> on each of its <i>SubCommands</i>
 * turn. Each <i>SubCommand</i> will be passed a reference to the original
 * <code>INotification</code> that was passed to the <code>MacroCommand</code>'s
 * <code>execute</code> method.
 * </P>
 * <p>
 * <p>
 * Unlike <code>SimpleCommand</code>, your subclass should not override
 * <code>execute</code>, but instead, should override the
 * <code>initializeMacroCommand</code> method, calling
 * <code>addSubCommand</code> once for each <i>SubCommand</i> to be executed.
 * </P>
 * <p>
 * <p>
 *
 * @see com.puremvc.core.Controller Controller
 * @see com.puremvc.patterns.observer.Notification Notification
 * @see com.puremvc.patterns.command.SimpleCommand SimpleCommand
 */
public class MacroCommand extends BaseNotifier implements Command {

    private Collection<Class<? extends Command>> subCommands = null;

    /**
     * Constructor.
     * <p>
     * <p>
     * You should not need to define a constructor, instead, override the
     * <code>initializeMacroCommand</code> method.
     * </P>
     * <p>
     * <p>
     * If your subclass does define a constructor, be sure to call
     * <code>super()</code>.
     * </P>
     */
    public MacroCommand() {
        subCommands = new Vector<>();
        initializeMacroCommand();
    }

    /**
     * Initialize the <code>MacroCommand</code>.
     * <p>
     * <p>
     * In your subclass, override this method to initialize the
     * <code>MacroCommand</code>'s <i>SubCommand</i> list with
     * <code>ICommand</code> class references like this:
     * </P>
     * <p>
     * <listing> // Initialize MyMacroCommand override protected function
     * initializeMacroCommand( ) : void { addSubCommand(
     * com.me.myapp.controller.FirstCommand ); addSubCommand(
     * com.me.myapp.controller.SecondCommand ); addSubCommand(
     * com.me.myapp.controller.ThirdCommand ); } </listing>
     * <p>
     * <p>
     * Note that <i>SubCommand</i>s may be any <code>ICommand</code>
     * implementor, <code>MacroCommand</code>s or <code>SimpleCommands</code>
     * are both acceptable.
     */
    protected void initializeMacroCommand() {
    }

    /**
     * Add a <i>SubCommand</i>.
     * <p>
     * <p>
     * The <i>SubCommands</i> will be called in First In/First Out (FIFO)
     * order.
     * </P>
     *
     * @param commandClassRef a reference to the <code>Class</code> of the
     *                        <code>ICommand</code>.
     */
    protected void addSubCommand(Class<? extends Command> commandClassRef) {
        subCommands.add(commandClassRef);
    }

    /**
     * Execute this <code>MacroCommand</code>'s <i>SubCommands</i>.
     * <p>
     * <p>
     * The <i>SubCommands</i> will be called in First In/First Out (FIFO)
     * order.
     *
     * @param notification the <code>INotification</code> object to be passsed to each
     *                     <i>SubCommand</i>.
     */
    public void execute(Notification notification) {
        for (Class<? extends Command> commandClass : subCommands) {
            try {
                Command command = commandClass.newInstance();
                command.execute(notification);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
