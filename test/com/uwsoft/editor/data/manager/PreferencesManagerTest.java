package com.uwsoft.editor.data.manager;

import com.runner.LibgdxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class PreferencesManagerTest {
    private PreferencesManager preferencesManager;

    @Before
    public void setUp() throws Exception {
        preferencesManager = new PreferencesManager();
        preferencesManager.buildRecentHistory();
    }

    @Test
    public void shouldGetEmptyHistory() throws Exception {
        List<String> recentHistory = preferencesManager.getRecentHistory();

        assertThat(recentHistory.size(), is(0));
    }

    @Test
    public void shouldAbleToPushHistory() throws Exception {
        preferencesManager.pushHistory("testfile");
        List<String> recentHistory = preferencesManager.getRecentHistory();

        assertThat(recentHistory.size(), is(1));
        assertThat(recentHistory, hasItem("testfile"));
    }

    @Test
    public void shouldRemoveHistoryAfterPop() throws Exception {
        preferencesManager.pushHistory("testfile");
        List<String> recentHistory = preferencesManager.getRecentHistory();
        assertThat(recentHistory.size(), is(1));

        preferencesManager.popHistory("testfile");

        recentHistory = preferencesManager.getRecentHistory();

        assertThat(recentHistory.size(), is(0));
    }

    @Test
    public void shouldGetEmptyListAfterCleanHistory() throws Exception {
        preferencesManager.pushHistory("testfile1");
        preferencesManager.pushHistory("testfile2");
        preferencesManager.pushHistory("testfile3");
        List<String> recentHistory = preferencesManager.getRecentHistory();
        assertThat(recentHistory.size(), is(3));

        preferencesManager.clearHistory();

        recentHistory = preferencesManager.getRecentHistory();

        assertThat(recentHistory.size(), is(0));
    }
}