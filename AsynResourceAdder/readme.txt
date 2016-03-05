提供游戏中的异步增加资源接口来应对如战斗这些低延迟的场景。

com.asynQueue.QueueRunner 是一个代表资源的增减task:

	1.模仿netty加了一个watcher的原子哨兵Watcher,当QueueHandleManager中该任务存在且未被处理时不添加进队列,避免队列遍历线程执行时间过长
	用。
	if(watcher.compareAndSet(QueuedState.OFF_THE_QUEUE.getValue(), QueuedState.IN_THE_QUEUE.getValue())){
		manager.addToQueue(this);
	}

	watcher.compareAndSet(QueuedState.IN_THE_QUEUE.getValue(), QueuedState.OFF_THE_QUEUE.getValue());
	handleAccumaleteData();



	2.原子变量 changeDataValue 用来玩家增加或减少的资源数目，同时可以实现无锁的资源变化和数据处理
	private long noLockGetData(){
		for(;;){
			long before = changeDataValue.get();
			if(changeDataValue.compareAndSet(before, 0)){
				return before;
			}
		}
	}

com.asynQueue.QueueExecutor 资源的实际执行线程池
	1.提供对线程池的关闭，和遍历资源线程的interrupted关闭


com.asynQueue.PlayerAsyResourceManager 异步玩家管理类整个核心逻辑的外壳

TestCase: 提供的测试用例



