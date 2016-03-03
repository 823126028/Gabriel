package com.runnable;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 控制每种key的可执行队列只能同时存在指定的数量,并且要按顺序执行。
 * 可用于游戏服务器Netty异步处理客户端数据的组建(同一个玩家只能存在指定数量的包没有进行处理,超过的直接丢弃,防止游戏

负载过重或外挂恶意破坏)
 * @author Gabriel
 *
 */
public class CountAwareOrderedRunnerExecutor extends ThreadPoolExecutor{
	private int maxCount;
	/**存储单个KEY的可执行数目*/
	private ConcurrentMap<String, AtomicInteger> countMap = new ConcurrentHashMap<String, AtomicInteger>();
	/**每个key的内部执行器,每个key一个内部执行器，来使里面的任务按顺序执行*/
	private ConcurrentMap<String, InnerRunnerExecutor> executorMap = new ConcurrentHashMap<String, 

InnerRunnerExecutor>();
	
	/**
	 * @param coreSize
	 * @param maxSize
	 * @param maxCount	(单个Key可存储的最大执行任务)
	 * @param totalMaxCount (父ThreadPool中存在的未执行的所有子ThreadPool的数量(超出直接丢弃DiscardPolicy())
	 */
	public CountAwareOrderedRunnerExecutor(int coreSize,int maxSize,int maxCount,int totalMaxCount) {
		super(coreSize, maxSize, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(totalMaxCount));
		this.maxCount = maxCount;
		this.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
	}
	
	@Override
	public void execute(Runnable runnable) {
		String runnableKey = ((CountAwareRunner)runnable).getKey();
		if(runnable instanceof CountAwareRunner){
			if (needReject(runnableKey)) {
				System.out.println("==============reject");
				return;
			}
			InnerRunnerExecutor innerRunnerExecutor = executorMap.get(runnableKey);
			if(innerRunnerExecutor == null){
				innerRunnerExecutor = new InnerRunnerExecutor();
				InnerRunnerExecutor oldExecutor = executorMap.putIfAbsent(runnableKey, 

innerRunnerExecutor);
				if(oldExecutor != null){
					innerRunnerExecutor = oldExecutor;
				}
			}
			innerRunnerExecutor.execute(runnable);
		}
		else{
			super.execute(runnable);
		}
	}
	
	/**
	 * 判断是否会拒绝该任务
	 * @param runnableKey
	 * @return
	 */
	private boolean needReject(String runnableKey){
		AtomicInteger countNum = countMap.get(runnableKey);
		if(countNum == null){
			countNum = new AtomicInteger(0);
			/**利用putIfAbsent的特性,返回之前的对象，如果之前的对象不为null那么直接用之前的对象*/
			AtomicInteger oldNum = countMap.putIfAbsent(runnableKey, countNum);
			if(oldNum != null){
				countNum = oldNum;
			}
		}
		if(countNum.get() >= maxCount)
			return true;
		countNum.incrementAndGet();
		return false;
	}
	
	private void doExecute(Runnable r){
		super.execute(r);
	}
	
	private class InnerRunnerExecutor implements Executor,Runnable {
		private final LinkedList<Runnable> tasks = new LinkedList<Runnable>();

		public void run() {
			for(;;){
				
				Runnable task = null;
				/** 因为empty是临界值所以不能把它取空 ,所以不能用poll,只能用get，执行之后再remove掉

*/
				/** 如果Empty了,那么直接会再使用父线程池的另一个线程运行这一个Run任务*/
				synchronized (tasks) {
					task = tasks.getFirst();
				}
				String runnableKey = ((CountAwareRunner)task).getKey();
				AtomicInteger countNum = countMap.get(runnableKey);
				countNum.decrementAndGet();
				boolean ran = false;
				try{
					task.run();
					ran = true;
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					synchronized (tasks) {
						tasks.removeFirst();
						if(tasks.isEmpty()){
							break;
						}
					}
				}
			}
		}

		@Override
		public void execute(Runnable runnable) {
			boolean isEmpty = true;
			synchronized (tasks) {
				 isEmpty = tasks.isEmpty();
				tasks.add(runnable);
			}
			//如果是空的才会重新执行一个执行线程
			if(isEmpty){
				doExecute(this);
			}
		}
		
	}

}
