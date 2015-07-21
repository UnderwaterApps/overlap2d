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

import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.openal.AL;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Created by azakhary on 5/15/2015.
 */
public class SplashStarter {

    private SplashScreen splashScreen;
    private SplashFrame splashFrame;

    public SplashStarter(SplashScreen.SplashListener listener) {

        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_UNIX) {
            // let's work out osx splash screen later, not sure if we can have translucency there.
            listener.loadingComplete();
            return;
        }

        splashScreen = new SplashScreen();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        config.width = 467;
        config.height = 415;
        config.resizable = false;
        splashFrame = new SplashFrame(splashScreen, config);
        /*
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isUniformTranslucencySupported = gd.isWindowTranslucencySupported(TRANSLUCENT);
        boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
        boolean isShapedWindowSupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);
        */

        splashScreen.listener = listener;
    }

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public void kill() {
        splashScreen.dispose();
        splashFrame.dispose();
        AL.destroy();
    }
}
