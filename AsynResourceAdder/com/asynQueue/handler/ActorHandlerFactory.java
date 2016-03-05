package com.asynQueue.handler;

import com.asynQueue.handler.comm.FoodHandler;
import com.asynQueue.handler.comm.IronHandler;
import com.asynQueue.handler.comm.LumberHandler;

public class ActorHandlerFactory {
	public static ActorHandler getActorByType(String type,long pid){
		if(type.equals(ActorType.IRON.getType()))
			return new IronHandler(pid, type);
		else if(type.equals(ActorType.LUMBER.getType()))
			return new LumberHandler(pid, type);
		else if(type.equals(ActorType.FOOD.getType()))
			return new FoodHandler(pid,type);
		return  null;
	}
	
	public static enum ActorType{
		IRON("iron"),
		FOOD("food"),
		LUMBER("lumber");
		private String type;
		ActorType(String type){
			this.setType(type);
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
