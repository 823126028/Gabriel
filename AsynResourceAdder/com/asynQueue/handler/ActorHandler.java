package com.asynQueue.handler;

public abstract class ActorHandler {
	protected ActorHandler(long pid,String type){}
	public abstract void handleDelta(long delta);

}
