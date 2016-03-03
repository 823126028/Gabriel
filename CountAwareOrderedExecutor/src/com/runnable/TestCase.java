package com.runnable;

public class TestCase {
	public static CountAwareOrderedRunnerExecutor care = new CountAwareOrderedRunnerExecutor(1,10, 5, 5000);
	public static class TestThread extends Thread{
		public void run(){
			for(int i = 0; i <= 10;i++){
				CountAwareRunner car = new CountAwareRunner("" + 1);
				care.execute(car);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args){
		new TestThread().start();	
	}
}
