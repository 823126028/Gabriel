package com.asynQueue;

import com.asynQueue.handler.comm.FoodHandler;

public class TestClass {
	/**test key*/
	public static final String key = "10091_food";
	
	public static class TestThread extends Thread{
		public void run(){
			for(int i = 0; i <10000; i++){
				QueueRunner qr = PlayerAsyResourceManager.getInstance().getQueueRunnerByKey(key);
				qr.changeData(2);
				Thread.yield();
			}
		}
	}
	public static void main(String[] args){
		new TestThread().start();
		new TestThread().start();
		new TestThread().start();
		new TestThread().start();
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		QueueRunner qr = PlayerAsyResourceManager.getInstance().getQueueRunnerByKey(key);
		FoodHandler i = (FoodHandler) qr.getDelegateHandler();
		i.print0();
		PlayerAsyResourceManager.getInstance().shutDown();
	}
}
