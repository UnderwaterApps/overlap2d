package com.runner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.kotcrab.vis.ui.VisUI;
import com.runner.exception.LibgdxInitException;
import com.runner.util.ConditionWaiter;
import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibgdxRunner extends BlockJUnit4ClassRunner {
    private Random random = new Random();
    private static File prefs;
    private static AtomicBoolean init = new AtomicBoolean(false);

    public LibgdxRunner(Class<?> klass) throws InitializationError {
        super(klass);
        if (init.compareAndSet(false, true)) {
            initApplication();
        }
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
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                safeCleanDir();
                closeGdxApplication();
            }));
        } catch (Exception ex) {
            throw new LibgdxInitException(ex);
        }
    }

    private void safeCleanDir() {
        try {
            FileUtils.deleteDirectory(prefs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeGdxApplication() {
        Gdx.app.exit();
    }

    @Override
    public void run(RunNotifier notifier) {
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
            ConditionWaiter.wait(() -> !running.get(), description, 30, () -> {
                closeGdxApplication();
            });
        } else {
            runLeaf(methodBlock(method), description, notifier);
        }
    }

    private class TestApplicationListener extends ApplicationAdapter {
        @Override
        public void create() {
            VisUI.load(Gdx.files.local("overlap2d/assets/style/uiskin.json"));
        }
    }
}
