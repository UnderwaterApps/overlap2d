package com.runner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.runner.exception.LibgdxInitException;
import com.runner.util.ConditionWaiter;
import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibgdxRunner extends BlockJUnit4ClassRunner {
    private File prefs;
    private Random random = new Random();

    public LibgdxRunner(Class<?> klass) throws InitializationError {
        super(klass);
        initApplication();
    }

    private void initApplication() {
        try {
            JglfwApplicationConfiguration cfg = new JglfwApplicationConfiguration();
            cfg.preferencesLocation = String.format("tmp/%d/.prefs/", random.nextLong());
            cfg.title = "Libgdx Runner";
            cfg.width = 1;
            cfg.height = 1;
            cfg.forceExit = true;
            new JglfwApplication(new TestApplicationListener(), cfg);
            ConditionWaiter.wait(() -> Gdx.files != null, "Jglfw init failed.", 10);
            prefs = new File(Gdx.files.getExternalStoragePath(), "tmp/");
            safeCleanDir();
        } catch (Exception ex) {
            throw new LibgdxInitException(ex);
        }
    }

    public void safeCleanDir() {
        try {
            FileUtils.deleteDirectory(prefs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new CloseLibgdxListener());
        super.run(notifier);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (description.getAnnotation(NeedGL.class) != null) {
            final AtomicBoolean running = new AtomicBoolean(true);
            Gdx.app.postRunnable(() -> {
                if (isIgnored(method)) {
                    notifier.fireTestIgnored(description);
                } else {
                    runLeaf(methodBlock(method), description, notifier);
                }
                running.set(false);
            });
            ConditionWaiter.wait(() -> !running.get(), description, 30);
        } else {
            runLeaf(methodBlock(method), description, notifier);
        }
    }

    private class TestApplicationListener extends ApplicationAdapter {

    }

    private class CloseLibgdxListener extends RunListener {
        @Override
        public void testRunFinished(Result result) throws Exception {
            Gdx.app.exit();
            ConditionWaiter.wait(() -> Gdx.app == null, "Gdx exit failed", 10);
        }
    }
}
