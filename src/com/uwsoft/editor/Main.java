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
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.commons.lang3.SystemUtils;

import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.uwsoft.editor.utils.AppConfig;

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
            config.width = (int) (width);
            config.height = (int) (height - height * .04);
            config.backgroundFPS = 0;
            config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
            new JglfwApplication(overlap2D, config);
        } else {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
            config.width = (int) (width);
            config.height = (int) (height - height * .04);
            config.backgroundFPS = 0;
            LwjglFrame mainFrame = new LwjglFrame(overlap2D, config);
            mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }

    }

    public static void main(String[] argv) throws Exception {
        String input = "../art/textures";
        String output = "style";
        String packFileName = "uiskin";
        TexturePacker.Settings settings =  new TexturePacker.Settings();
        settings.flattenPaths = true;
        TexturePacker.processIfModified(input, output, packFileName);
        new Main();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

}
