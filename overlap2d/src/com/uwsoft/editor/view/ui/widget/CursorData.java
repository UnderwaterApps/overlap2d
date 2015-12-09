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

package com.uwsoft.editor.view.ui.widget;


/**
 * Created by azakhary on 5/15/2015.
 */
public class CursorData {

    public String region;
    public int hotspotX;
    public int hotspotY;

    public CursorData(String region, int x, int y) {
        this.region = region;
        hotspotX = x;
        hotspotY = y;
    }

    public int getHotspotX() {
        return hotspotX;
    }

    public int getHotspotY() {
        return hotspotY;
    }
}
