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

package com.commons.color;

import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.i18n.BundleText;

/**
 * Contains texts for chooser access via I18NBundle
 * @since 0.7.0
 */
public enum ColorPickerText implements BundleText {
    // @formatter:off
    TITLE			 		{public String getName () {return "title";}},
    RESTORE					{public String getName () {return "restore";}},
    CANCEL 					{public String getName () {return "cancel";}},
    OK						{public String getName () {return "ok";}},
    NEW 					{public String getName () {return "new";}},
    OLD 					{public String getName () {return "old";}},
    HEX 					{public String getName () {return "hex";}};
    // @formatter:on

    @Override
    public String get () {
        return VisUI.getColorPickerBundle().get(getName());
    }

    @Override
    public String format () {
        throw new UnsupportedOperationException();
    }

    @Override
    public String format (Object... arguments) {
        throw new UnsupportedOperationException();
    }
}
