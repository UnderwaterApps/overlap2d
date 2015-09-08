package com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.runner.LibgdxRunner;
import org.apache.commons.io.FileUtils;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Use {@link LibgdxRunner LibgdxRunner}
 **/
@Deprecated
public class LibgdxRule extends ExternalResource {
    private Random random = new Random();
    private File externalStoragePath;

    @Override
    protected void before() throws Throwable {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.preferencesDirectory = String.format("tmp/%d/.prefs/", random.nextLong());
        new LwjglApplication(new TestApplicationListener(), cfg);
        externalStoragePath = new File(Gdx.files.getExternalStoragePath(), "tmp/");
    }

    @Override
    protected void after() {
        try {
            FileUtils.deleteDirectory(externalStoragePath);
        } catch (IOException e) {

        }
    }

    class TestApplicationListener extends ApplicationAdapter {
        @Override
        public void render() {
            System.out.printf(Gdx.gl.toString());
        }
    }
}
