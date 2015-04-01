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

package com.uwsoft.editor.gdx.tween;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.gdx.stage.BaseStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TweenManager {
	//public GameStage stage;
	public HashMap<String, TimeLineVO> tweenMap = new HashMap<String, TimeLineVO>(1);
	
	public TweenManager(BaseStage s){
		//stage = s;
		//for(int i=0;i<stage.dataManager.curentLevel.tweens.size();i++){
		//	TimeLineVO tvo =stage.dataManager.curentLevel.tweens.get(i);
		//	tweenMap.put(tvo.tweenId, tvo);
		//}
	}//
	
	public void startTween(String tweenId){
		TimeLineVO tvo = tweenMap.get(tweenId);
		if(tvo != null && tvo.tweens != null && tvo.tweens.size() > 0) {
			//TimeLineSequence reversiableTimeLine = TimeLineSequence.$(tvo); 
			//Actor actor = stage.findActor(tvo.tweenObjectName);		
			//actor.addAction(reversiableTimeLine);
		}
	}
	
	public void stopTween(String tweenId){
		//TODO
	}
	
	public void pauseTween(String tweenId){
		//TODO
	}
	
	public void getRuningTweens(Actor actor){
		//TODO
	}
	
	public ArrayList<TimeLineVO> getTweens(Actor actor){
		ArrayList<TimeLineVO> tweenArray = new ArrayList<TimeLineVO>(0);
		//String name = actor.name;
		Set<String> keys = tweenMap.keySet();
    	for (String cur : keys) {
    		TimeLineVO vo = tweenMap.get(cur);
    		//if(vo.tweenObjectName.equalsIgnoreCase(name)){
    		//	tweenArray.add(vo);
    		//}
		}
		return tweenArray;
	}
}
