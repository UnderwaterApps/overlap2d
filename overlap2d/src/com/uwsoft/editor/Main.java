/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.InputEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.uwsoft.editor.view.frame.FileDropListener;
import org.apache.commons.lang3.SystemUtils;

import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.uwsoft.editor.splash.SplashStarter;
import com.uwsoft.editor.utils.AppConfig;

public class Main {

    private SplashStarter splash;
    private LwjglFrame mainFrame;

    public Main() {
        splash = new SplashStarter(() -> startLoadingEditor());
    }

    private void startLoadingEditor() {
        //first, kill off the splash
    	if (!(SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_UNIX)) {
    		splash.kill();
    	}

        Overlap2D overlap2D = new Overlap2D();
        Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double width = maximumWindowBounds.getWidth();
        double height = maximumWindowBounds.getHeight();

        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Overlap2D");
            JglfwApplicationConfiguration config = new JglfwApplicationConfiguration();
            config.width = (int) (width);
            config.height = (int) (height - height * .04);
            config.backgroundFPS = 60;
            config.title = "Overlap2D - v" + AppConfig.getInstance().version;
            new JglfwApplication(overlap2D, config);
        } else {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "Overlap2D - v" + AppConfig.getInstance().version;
            config.fullscreen = true;
            config.resizable = false;
            config.width = (int) (width);
            config.height = (int) (height - height * .04);
            config.backgroundFPS = 60;
            mainFrame = new LwjglFrame(overlap2D, config);
            mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            toggleVisible();

            // subscribe to file dropping notifications, currently windows only
            DropTarget dropTarget = new DropTarget(mainFrame, new FileDropListener());
        }

        if(!SystemUtils.IS_OS_UNIX) {
            // no aesthetics for linux users I guess..
            setIcon();
        }

    }

    public static String getLocalArtPath(String directoryName) {
        // TODO: wtf with all the multiplatform shit? anyone had experience?
        URL inputUrl = Main.class.getClassLoader().getResource("art/"+directoryName);
        String input;
        if(inputUrl != null) {
            input = inputUrl.getPath();
        } else {
            inputUrl = Main.class.getClassLoader().getResource(directoryName);
            if(inputUrl != null) {
                input = inputUrl.getPath();
            } else {
                input = "overlap2d/art/" +directoryName;
            }
        }
        File file = new File(input);
        if(!file.exists()){
            input = "art/"+directoryName;
        }

        return input;
    }

    public static void main(String[] argv) throws Exception {
        /**
         * this should not be happening when in release mode
         */
         /**
        String input = getLocalArtPath("textures");
        String output = "style";
        String packFileName = "uiskin";
        TexturePacker.Settings settings =  new TexturePacker.Settings();
        settings.flattenPaths = true;
        TexturePacker.processIfModified(input, output, packFileName);
        processSplashScreenTextures();
        */

        new Main();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private static void processSplashScreenTextures() {
        String input = getLocalArtPath("splash_textures");

        String output = "splash";
        String packFileName = "splash";
        TexturePacker.Settings settings =  new TexturePacker.Settings();
        settings.flattenPaths = true;
        TexturePacker.processIfModified(input, output, packFileName);
    }


    private void toggleVisible() {
        mainFrame.setVisible(!mainFrame.isVisible());
        if (mainFrame.isVisible()) {
            mainFrame.toFront();
            mainFrame.requestFocus();
            mainFrame.setAlwaysOnTop(true);
            try {
                //remember the last location of mouse
                final Point oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

                //simulate a mouse click on title bar of window
                Robot robot = new Robot();
                robot.mouseMove(mainFrame.getX() + 100, mainFrame.getY() + 5);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                //move mouse to old location
                robot.mouseMove((int) oldMouseLocation.getX(), (int) oldMouseLocation.getY());
            } catch (Exception ex) {
                //just ignore exception, or you can handle it as you want
            } finally {
                mainFrame.setAlwaysOnTop(false);
            }
        }
    }

    //THIS IS JUST FOR FUN
    private void setIcon(){
    	String logoPath = getLocalArtPath("splash_textures");
        File file = new File(logoPath);
        if(!file.exists()){
        	 logoPath = "art/splash_textures/";
             file = new File(logoPath);
        }
        logoPath+="icon.png";

    	if(mainFrame != null){
    		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(logoPath));
    		return;
    	}

    	 try {
             Class util = Class.forName("com.apple.eawt.Application");
             Method getApplication = util.getMethod("getApplication", new Class[0]);
             Object application = getApplication.invoke(util);
             Class params[] = new Class[1];
             params[0] = Image.class;
             Method setDockIconImage = util.getMethod("setDockIconImage", params);
             Image image = Toolkit.getDefaultToolkit().getImage(logoPath);
             setDockIconImage.invoke(application, image);
         } catch (ClassNotFoundException e) {
             // log exception
         } catch (NoSuchMethodException e) {
             // log exception
         } catch (InvocationTargetException e) {
             // log exception
         } catch (IllegalAccessException e) {
             // log exception
         }
    }
}
