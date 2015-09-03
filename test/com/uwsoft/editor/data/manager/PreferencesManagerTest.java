package com.uwsoft.editor.data.manager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PreferencesManagerTest {
    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();
    private static File file;

    private PreferencesManager preferencesManager;

    @BeforeClass
    public static void setUpLibgdxEnv() throws IOException {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        file = PreferencesManagerTest.folder.newFolder();
        cfg.preferencesDirectory = folder.toString();
        new LwjglApplication(new TestApplicationListener(), cfg);
    }

    @AfterClass
    public static void cleanLibgdxEnv() {
        Gdx.app.exit();
    }

    @Before
    public void setUp() throws Exception {
        file.delete();
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

    static class TestApplicationListener extends ApplicationAdapter {

    }
}