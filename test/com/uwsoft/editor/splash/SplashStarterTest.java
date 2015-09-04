package com.uwsoft.editor.splash;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SplashStarterTest {
    private SplashStarter splashStarter;
    @Mock
    private SplashScreen.SplashListener listener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        splashStarter = new SplashStarter(listener);
    }

    @Test
    public void shouldClassLoadCompleteIfNotSplashScreen() throws Exception {
        SplashScreen splashScreen = splashStarter.getSplashScreen();

        if (splashScreen != null) {
            verify(listener, never()).loadingComplete();
        } else {
            verify(listener).loadingComplete();
        }
    }
}