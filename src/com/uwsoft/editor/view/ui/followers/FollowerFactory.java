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

package com.uwsoft.editor.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 5/21/2015.
 */
public class FollowerFactory {

    public static BasicFollower createFollower(Entity entity) {
        switch (EntityUtils.getType(entity)) {
            case EntityFactory.IMAGE_TYPE:
                return new ImageFollower(entity);
            case EntityFactory.LIGHT_TYPE:
                return new LightFollower(entity);
            case EntityFactory.LABEL_TYPE:
                return new LabelFollower(entity);
            case EntityFactory.PARTICLE_TYPE:
                return new ParticleFollower(entity);
        }

        return new NormalSelectionFollower(entity);
    }
}
