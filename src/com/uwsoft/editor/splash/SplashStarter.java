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

package com.uwsoft.editor.splash;

import com.badlogic.gdx.backends.lwjgl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

/**
 * Created by azakhary on 5/15/2015.
 */
public class SplashStarter {

    public SplashStarter() {
        super();
        initSplashScreen();
    }

    private void initSplashScreen() {
        SplashScreen splashScreen = new SplashScreen();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        config.width = 467;
        config.height = 415;
        config.resizable = false;
        SplashFrame splashFrame = new SplashFrame(splashScreen, config);

        /*
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isUniformTranslucencySupported = gd.isWindowTranslucencySupported(TRANSLUCENT);
        boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
        boolean isShapedWindowSupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);
        */
    }
}
