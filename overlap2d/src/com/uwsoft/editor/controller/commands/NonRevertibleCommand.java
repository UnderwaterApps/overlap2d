package com.uwsoft.editor.controller.commands;

import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.controller.SandboxCommand;
import com.uwsoft.editor.proxy.CommandManager;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

import java.util.HashMap;

/**
 * Created by azakhary on 11/29/2015.
 */
public abstract class NonRevertibleCommand extends SandboxCommand {

    protected CommandManager commandManager;
    protected Notification notification;

    protected boolean isCancelled = false;
    protected final HashMap<String, CompositeItemVO> libraryItems;
    protected final ProjectManager projectManager;

    public NonRevertibleCommand() {
        this.projectManager = facade.retrieveProxy(ProjectManager.NAME);
        this.libraryItems = projectManager.getCurrentProjectInfoVO().libraryItems;
    }

    @Override
    public void execute(Notification notification) {
        commandManager = facade.retrieveProxy(CommandManager.NAME);
        this.notification = notification;
        callDoAction();
        if (!isCancelled) commandManager.clearHistory();
    }

    public abstract void doAction();

    public void callDoAction() {
        doAction();
    }

    public void cancel() {
        isCancelled = true;
    }
}
