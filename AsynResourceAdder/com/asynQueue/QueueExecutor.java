package com.asynQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 资源的实际执行线程池
 * @author Gabriel
 *
 */
public class QueueExecutor {
	private LinkedBlockingQueue<QueueRunner> queue = new LinkedBlockingQueue<QueueRunner>();
	private ExecutorService ext = Executors.newFixedThreadPool(5);
	private volatile boolean init = false;
	private volatile boolean shutDown = false;
	private RunThread runThread;
	
	/**
	 * 向线程池中增加执行对象。如果已经关闭了就不进行添加
	 * @param queueRunner
	 */
	public void addToQueue(QueueRunner queueRunner){
		if(shutDown){
			return;
		}
		try {
			queue.put(queueRunner);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭执行线程池(被RunThread确认队列中没有task之后调用,如果该方法被提前调用
	 * 将导致线程池关闭task被拒绝执行)
	 */
	private void shutDownExecutors(){
		ext.shutdown();
		try {
			ext.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 暴露在外面的Task接口
	 */
	public void shutDown(){
		shutDown = true;
		//打断阻塞中的线程进行关闭
		if(runThread != null)
			runThread.interrupt();
	}
	
	private class RunThread extends Thread{
		private boolean checkNeedShutDown(){
			if(queue.isEmpty() && shutDown)
				return true;
			return false;
		}
		public void run(){
			QueueRunner runTask = null;
			for (;;) {
				try {
					runTask = queue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(runTask != null)
					ext.submit(runTask);
				
				//检查任务队列为空停止执行
				if(checkNeedShutDown()){
					shutDownExecutors();
					break;
				}
					
			}
			System.out.println("foreach Thread ShutDown");
		}
	}
	
	public void init(){
		if(!init){
			runThread = new RunThread();
			runThread.start();
		}
	}
}
