package com.uwsoft.editor;

import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.uwsoft.editor.gdx.Overlap2D;
import com.uwsoft.editor.utils.AppConfig;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;

public class Main {

    public Main() {
        Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double width = maximumWindowBounds.getWidth();
        double height = maximumWindowBounds.getHeight();
        Overlap2D overlap2D = new Overlap2D();
        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Overlap2D");
            JglfwApplicationConfiguration config = new JglfwApplicationConfiguration();
            config.width = (int) width;
            config.height = (int) height;
            config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
            new JglfwApplication(overlap2D, config);
        } else {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.width = (int) width;
            config.height = (int) height;
            config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
            new LwjglApplication(overlap2D, config);
        }

    }

    public static void main(String[] argv) throws Exception {
        new Main();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

}
