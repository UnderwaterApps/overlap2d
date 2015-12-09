package com.puremvc.patterns.command;

import com.puremvc.patterns.observer.BaseNotification;
import com.puremvc.patterns.observer.Notification;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MacroCommandTest {
    private MacroCommand macroCommand;

    @Before
    public void setUp() throws Exception {
        macroCommand = new MacroCommand();
    }

    @Test
    public void shouldCallAllSubCommand() throws Exception {
        macroCommand.addSubCommand(TestCommandOne.class);
        macroCommand.addSubCommand(TestCommandTwo.class);

        assertThat(getSubCommands().size(), is(2));

        BaseNotification notification = new BaseNotification("baseNotification");
        macroCommand.execute(notification);

        assertThat(TestCommandOne.executed, is(true));
        assertThat(TestCommandTwo.executed, is(true));
        assertThat(TestCommandTwo.notification, is(notification));
    }

    private Collection<Class<? extends Command>> getSubCommands() {
        return (Collection<Class<? extends Command>>) Whitebox.getInternalState(macroCommand, "subCommands");
    }

    static class TestCommandOne extends SimpleCommand {
        public static boolean executed = false;

        @Override
        public void execute(Notification notification) {
            executed = true;
        }
    }

    static class TestCommandTwo extends SimpleCommand {
        public static boolean executed = false;
        public static Notification notification;

        @Override
        public void execute(Notification notification) {
            executed = true;
            TestCommandTwo.notification = notification;
        }
    }
}