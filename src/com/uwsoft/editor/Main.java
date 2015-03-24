package com.uwsoft.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.uwsoft.editor.gdx.Overlap2D;
import com.uwsoft.editor.utils.AppConfig;
import com.uwsoft.editor.utils.OSType;

import javax.swing.*;
import java.awt.*;

public class Main {

    public Main() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double width = maximumWindowBounds.getWidth();
        double height = maximumWindowBounds.getHeight();
        config.width = (int) width;
        config.height = (int) height;
        config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
        Overlap2D overlap2D = new Overlap2D();
        if (OSType.getOS_Type() == OSType.MacOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Overlap2D");
        }
        else{
            new LwjglApplication(overlap2D, config);
        }

    }

    public static void main(String[] argv) throws Exception {
        new Main();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//
//        //Get project version
//        //System.out.println("Current Build Version: " + AppConfig.getInstance().version);
//
//        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
//
//        listener = new Overlap2DListener();
//        if (OSType.getOS_Type() == OSType.MacOS) {
//            System.setProperty("apple.laf.useScreenMenuBar", "true");
//            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Overlap2D");
//        }
//
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException
//                | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        final UIController uc = new UIController(listener);
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//
//
//                if (OSType.getOS_Type() == OSType.MacOS) {
//                    uc.initMacOSFrame();
//                } else {
//                    uc.initOtherOSFrame();
//                }
//                //uc.initUI();
//                //uc.addMainComponent();
//            }
//        });
    }

}
