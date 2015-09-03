package com.uwsoft.editor.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AppConfigTest {
    private AppConfig appConfig;

    @Before
    public void setUp() throws Exception {
        appConfig = AppConfig.getInstance();
    }

    @Test
    public void shouldGetRightVersion() throws Exception {
        assertThat(appConfig.version, is("0.1.1"));
    }
}