package com.puremvc.core;

import com.puremvc.patterns.proxy.Proxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CoreModelTest {
    private CoreModel coreModel;

    @Mock
    private Proxy proxy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        coreModel = new CoreModel();
        given(proxy.getProxyName()).willReturn("defaultProxyName");
    }

    @Test
    public void shouldAbleToRegisterProxyWithProxyName() throws Exception {
        coreModel.registerProxy(proxy);

        Map<String, Proxy> proxyMap = getProxyMap();
        assertThat(proxyMap.size(), is(1));
        assertThat(proxyMap.get("defaultProxyName"), is(proxy));
        verify(proxy).onRegister();
    }

    @Test
    public void shouldAbleToRemoveProxy() throws Exception {
        coreModel.registerProxy(proxy);

        coreModel.removeProxy("defaultProxyName");

        Map<String, Proxy> proxyMap = getProxyMap();
        assertThat(proxyMap.size(), is(0));
        verify(proxy).onRemove();
    }

    private Map<String, Proxy> getProxyMap() {
        return (Map<String, Proxy>) Whitebox.getInternalState(coreModel, "proxyMap");
    }
}