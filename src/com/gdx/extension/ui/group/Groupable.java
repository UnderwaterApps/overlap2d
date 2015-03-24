/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.ui.group;


/**
 * Used to make an {@link Actor actor} groupable by a {@link GroupActor}.
 * 
 * @author Kyu
 *
 */
public interface Groupable
{

    /** Used internally */
    public void setActorGroup(ActorGroup<? extends Groupable> group);
    
    /**
     * @return the {@link ActorGroup} of this {@link Actor actor}
     */
    public ActorGroup<? extends Groupable> getActorGroup();
    
    /**
     * @return the checked state of this {@link Actor actor}
     */
    public boolean isChecked();
    
    /**
     * Set the checked state of this {@link Actor actor}.
     * 
     * @param isChecked checked state
     */
    public void setChecked(boolean isChecked);
    
    /**
     * @return the state of this {@link Actor actor}
     */
    public boolean isDisabled();
    
    /**
     * Set the state of this {@link Actor actor}.
     * 
     * @param isDisabled the state
     */
    public void setDisabled(boolean isDisabled);
    
}
