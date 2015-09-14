package com.puremvc.core;

import com.puremvc.patterns.command.Command;
import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Observer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CoreView.class)
public class CoreControllerTest {
    private CoreController controller;

    @Mock
    private CoreView coreView;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(CoreView.class);
        PowerMockito.when(CoreView.getInstance()).thenReturn(coreView);
        controller = new CoreController();
    }

    @Test
    public void shouldCreateObserverWhenTheCommandFirstRegistered() throws Exception {
        controller.registerCommand("testCommand", TestCommand.class);

        verify(coreView).registerObserver(eq("testCommand"), any(Observer.class));
    }

    @Test
    public void shouldNotCreateObserverWhenCommandIsRegisteredAgain() throws Exception {
        controller.registerCommand("testCommand", TestCommand.class);
        reset(coreView);

        controller.registerCommand("testCommand", TestCommand.class);

        verify(coreView, never()).registerObserver(eq("testCommand"), any(Observer.class));
    }

    @Test
    public void shouldRemoveObserverWhenTheCommandIsRemoved() throws Exception {
        controller.registerCommand("testCommand", TestCommand.class);

        controller.removeCommand("testCommand");
        verify(coreView).removeObserver(eq("testCommand"), any(Notification.class));
    }

    class TestCommand implements Command {
        @Override
        public void execute(Notification notification) {

        }

        @Override
        public void sendNotification(String notificationName, Object body, String type) {

        }

        @Override
        public void sendNotification(String notificationName, Object body) {

        }

        @Override
        public void sendNotification(String notificationName) {

        }
    }
}