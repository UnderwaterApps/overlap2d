package com.puremvc.core;

import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.observer.BaseNotification;
import com.puremvc.patterns.observer.BaseObserver;
import com.puremvc.patterns.observer.Notification;
import com.puremvc.patterns.observer.Observer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CoreViewTest {
    private CoreView coreView;

    @Mock
    private BaseObserver observer;
    @Mock
    private Notification notification;
    @Mock
    private Mediator mediator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        coreView = new CoreView();
        given(mediator.getMediatorName()).willReturn("defaultMediatorName");
        given(mediator.listNotificationInterests()).willReturn(new String[]{});
    }

    @Test
    public void shouldRegisterObserverUnderGivenName() throws Exception {
        coreView.registerObserver("testCommand", observer);

        HashMap<String, List<Observer>> observerMap = getObserverMap();

        assertThat(observerMap.size(), is(1));
        assertThat(observerMap.get("testCommand").size(), is(1));
    }

    @Test
    public void shouldAbleToRemoveObserver() throws Exception {
        given(observer.compareNotifyContext(anyObject())).willReturn(true);
        coreView.registerObserver("commandNeedRemove", observer);
        coreView.removeObserver("commandNeedRemove", notification);

        HashMap<String, List<Observer>> observerMap = getObserverMap();
        assertThat(observerMap.size(), is(0));
    }

    @Test
    public void shouldNotifyAllObservers() throws Exception {
        BaseObserver observer1 = mock(BaseObserver.class);
        BaseObserver observer2 = mock(BaseObserver.class);
        BaseObserver observer3 = mock(BaseObserver.class);
        coreView.registerObserver("givenNotify", observer1);
        coreView.registerObserver("givenNotify", observer2);
        coreView.registerObserver("differentNotify", observer3);

        BaseNotification notification = new BaseNotification("givenNotify");
        coreView.notifyObservers(notification);

        verify(observer1).notifyObserver(notification);
        verify(observer2).notifyObserver(notification);
        verify(observer3, never()).notifyObserver(notification);
    }

    @Test
    public void shouldAbleToRegisterMediator() throws Exception {
        coreView.registerMediator(mediator);

        verify(mediator).onRegister();
    }

    @Test
    public void shouldNotRegisterForSameNameMediator() throws Exception {
        coreView.registerMediator(mediator);
        coreView.registerMediator(mediator);

        verify(mediator, times(1)).onRegister();
    }

    @Test
    public void shouldAbleToRemoveMediator() throws Exception {
        coreView.registerMediator(mediator);
        assertThat(getMediatorMap().size(), is(1));

        coreView.removeMediator("defaultMediatorName");
        assertThat(getMediatorMap().size(), is(0));
    }

    private HashMap<String, List<Observer>> getObserverMap() {
        return (HashMap<String, List<Observer>>) Whitebox.getInternalState(coreView, "observerMap");
    }

    private HashMap<String, Mediator> getMediatorMap() {
        return (HashMap<String, Mediator>) Whitebox.getInternalState(coreView, "mediatorMap");
    }
}