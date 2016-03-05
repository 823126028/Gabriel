package com.asynQueue.handler.comm;
import java.util.concurrent.atomic.AtomicLong;

import com.asynQueue.handler.ActorHandler;

public class FoodHandler extends ActorHandler{
	AtomicLong ai = new AtomicLong(0);

	public FoodHandler(long pid, String type) {
		super(pid, type);
	}

	@Override
	public void handleDelta(long delta) {
		ai.addAndGet(delta);
	}
	
	public void print0(){
		System.out.println(ai.get());
	}

}
