package com.uwsoft.editor.proxy;

import com.uwsoft.editor.controller.commands.RevertableCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommandManagerTest {
    private CommandManager commandManager;
    @Mock
    private RevertableCommand command;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        commandManager = new CommandManager();
        commandManager.onRegister();
    }

    @Test
    public void shouldAbleToAddCommand() throws Exception {
        commandManager.addCommand(command);
        commandManager.addCommand(command);

        int cursor = (int) Whitebox.getInternalState(commandManager, "cursor");
        ArrayList<RevertableCommand> commands = (ArrayList<RevertableCommand>) Whitebox.getInternalState(commandManager, "commands");

        assertThat(cursor, is(0));
        assertThat(commands, hasSize(2));
    }

    @Test
    public void shouldCallUndoActionIfCommandIsDone() throws Exception {
        given(command.isStateDone()).willReturn(true);
        commandManager.addCommand(command);

        commandManager.undoCommand();

        verify(command).callUndoAction();
        verify(command).setStateDone(eq(false));
    }

    @Test
    public void shouldCallRedoActionIfCommandIsNotDone() throws Exception {
        given(command.isStateDone()).willReturn(false);
        commandManager.addCommand(command);
        commandManager.addCommand(command);

        commandManager.redoCommand();

        verify(command).callDoAction();
        verify(command).setStateDone(eq(true));
    }

    @Test
    public void shouldDoNothingIfThereIsOnlyOneCommand() throws Exception {
        commandManager.addCommand(command);

        commandManager.redoCommand();

        verify(command, never()).callDoAction();
    }

    @Test
    public void shouldGetRightName() throws Exception {
        assertThat(CommandManager.NAME, is("com.uwsoft.editor.proxy.CommandManager"));
    }
}