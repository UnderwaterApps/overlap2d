package com.uwsoft.editor.gdx.tween;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.gdx.stage.BaseStage;

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
