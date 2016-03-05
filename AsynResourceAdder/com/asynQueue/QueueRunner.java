package com.asynQueue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.asynQueue.handler.ActorHandler;
import com.asynQueue.handler.ActorHandlerFactory;
/**
 * queue Runner 是一个资源,任务的增减对象,模仿Netty加了一个watcher的原子哨兵，
 * 当QueueHandleManager中该任务存在且未被处理的时不添加进队列,避免队列遍历线程执行时间过长。
 * @author Gabriel
 */
public class QueueRunner implements Runnable{
	
	/**父执行器*/
	protected QueueExecutor manager;
	
	/****playerId_resourceType  199_iron(资源:铁) 或者是199_kill(任务:杀敌)*/
	protected String key;

	/**哨兵(默认不在队列中)*/
	private AtomicInteger watcher = new AtomicInteger(QueuedState.OFF_THE_QUEUE.getValue());
	
	/**存储合并的值*/
	protected AtomicLong changeDataValue = new AtomicLong(0);
	
	/**实际处理的代理类*/
	protected ActorHandler delegateHandler;
	
	public ActorHandler getDelegateHandler(){
		return delegateHandler;
	}
	
	
	public QueueRunner(QueueExecutor manager,String key){
		this.manager = manager;
		this.key = key;
		String[] strs = key.split("_");
		long pid = Long.parseLong(strs[0]);
		String type = strs[1];
		delegateHandler = ActorHandlerFactory.getActorByType(type, pid);
	}
	/**
	 * @param data
	 * @param qhm
	 */
	public void changeData(int delataNum){
		//所有操作内部数据的接口如add必须是线程安全的
		changeDataValue.addAndGet(delataNum);
		
		if(watcher.compareAndSet(QueuedState.OFF_THE_QUEUE.getValue(), QueuedState.IN_THE_QUEUE.getValue())){
			manager.addToQueue(this);
		}
	}
	
	/**
	 * 通过对原子对象的循环操作实现无锁同步
	 * @return 从原子对象中取出的更改数据
	 */
	private long noLockGetData(){
		for(;;){
			long before = changeDataValue.get();
			if(changeDataValue.compareAndSet(before, 0)){
				return before;
			}
		}
	}
	
	public void run() {
		//将哨兵改为已经不在队列里了
		watcher.compareAndSet(QueuedState.IN_THE_QUEUE.getValue(), QueuedState.OFF_THE_QUEUE.getValue());
		handleAccumaleteData();
	}
	
	private void handleAccumaleteData(){
		long deltaValue = noLockGetData();
		//代理类执行所需要执行的操作
		delegateHandler.handleDelta(deltaValue);
	}
	
	public static enum QueuedState{
		IN_THE_QUEUE(0),
		OFF_THE_QUEUE(1);
		private int value;
		QueuedState(int value){
			this.setValue(value);
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}
}
