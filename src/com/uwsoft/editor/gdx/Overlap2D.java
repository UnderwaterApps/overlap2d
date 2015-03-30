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

package com.uwsoft.editor.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.screen.Overlap2DScreen;

public class Overlap2D extends ApplicationAdapter {

    public Screen currentScreen;

    public TextureManager textureManager;

    public Overlap2D() {

    }

    public void create() {
        VisUI.load();
        currentScreen = new Overlap2DScreen();
    }


    public void pause() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.pause();
    }

    public void resume() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.resume();
    }

    public void render() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void resize(int width, int height) {
        if (currentScreen == null) {
            return;
        }
        currentScreen.resize(width, height);
    }

    public void dispose() {
        VisUI.dispose();
        if (currentScreen == null) {
            return;
        }
        currentScreen.dispose();
    }

}
