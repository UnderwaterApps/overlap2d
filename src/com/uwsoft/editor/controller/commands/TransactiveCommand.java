package com.uwsoft.editor.controller.commands;

import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.observer.Notification;

/**
 * Created by CyberJoe on 7/25/2015.
 */
public abstract class TransactiveCommand extends RevertableCommand {

    protected Array<RevertableCommand> commands = new Array();

    @Override
    public void execute(Notification notification) {
        this.notification = notification;
        transaction();
        super.execute(notification);
    }

    @Override
    public void doAction() {
        for(int i = 0; i < commands.size; i++) {
            commands.get(i).callDoAction();
            if(commands.get(i).isCancelled) {
                // reverting
                for(int j = i-1; j >= 0; j--) {
                    commands.get(j).callUndoAction();
                }
                cancel();
                return;
            }
        }

        onFinish();
    }

    @Override
    public void undoAction() {
        for(int i = commands.size-1; i >= 0; i--) {
            commands.get(i).callUndoAction();
        }

        onFinishUndo();
    }

    public abstract void transaction();
    public abstract void onFinish();
    public abstract void onFinishUndo();

    protected void addInnerCommand(RevertableCommand command) {
        commands.add(command);
    }
}
