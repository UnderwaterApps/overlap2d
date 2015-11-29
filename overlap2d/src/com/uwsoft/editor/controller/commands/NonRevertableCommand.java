package com.uwsoft.editor.controller.commands;

import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.controller.SandboxCommand;
import com.uwsoft.editor.proxy.CommandManager;

/**
 * Created by azakhary on 11/29/2015.
 */
public abstract class NonRevertableCommand extends SandboxCommand {

    protected CommandManager commandManager;
    protected Notification notification;

    protected boolean isCancelled = false;

    @Override
    public void execute(Notification notification) {
        commandManager = facade.retrieveProxy(CommandManager.NAME);
        this.notification = notification;
        callDoAction();
        if(!isCancelled) commandManager.clearHistory();
    }

    public abstract void doAction();

    public void callDoAction() {
        doAction();
    }

    public void cancel() {
        isCancelled = true;
    }
}
