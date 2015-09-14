package com.uwsoft.editor.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String expectedVersion = StringUtils.EMPTY;
        String build = Files.toString(new File("build.gradle"), Charsets.UTF_8);
        Pattern pattern = Pattern.compile("version( {0,})=( {0,})'(\\d.\\d.\\d)'");
        Matcher matcher = pattern.matcher(build);
        if (matcher.find()) {
            expectedVersion = matcher.group(3);
        }
        assertThat(appConfig.version, is(expectedVersion));
    }
}