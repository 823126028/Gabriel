package com.gb.common.db.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue; 
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.gb.common.db.util.ConcurrentHashSet;

/**
 * 内存缓冲数据更新接口(将内存数据库作为主要存储对象，物理数据库只做落地)
 * 流程:
 * 		1.通过接口将POJO(id为key单个更新)提交到DB_OBJECT_MAP，以Class<?> 为key，(所有的同一个类型的数据在一个task里,Cache(操作同一个表))保证数据库和程序竞争最小，
 * 		    同时能最大化利用数据库的缓存。以conconcurrentLinked能保证不重复处理数据。(前提是输入的数据都是存储在内存
 * 		  数据库中的同一个对象)
 * 		2.指定的线程读取MAP(锁处理)，将数据传输到处理队列DbQueue，待数据库线程池处理。(处理报错的重试3次重新放入DbQueue等待处理)。
 * 		3.进程关闭的时候要让线程池等待所有的待执行MAP和DB Queue 全部执行完。
 * 		DBQUEUE可以分散到几个TASK里防止某个task时间太长
 * 		4.同时shutDOWN excutor的时候注意要wait一会儿
 * @author Gabriel Chen
 *
 */
public class MemDbService {
	/**如果待处理的Map为空，处理线程需要等待的时间*/
	private int WAIT_CACHE_SECOND = 500;
	
	private boolean shutDown = false;
	
	/**
	 * 数据处理的线程池
	 */
	private ThreadPoolExecutor dbExecutor;
	
	/**
	 * 存放缓存的数据操作的link(一个Runnable处理一中类型的所有数据按顺序处理，防止出现竞争)
	 */
	private LinkedBlockingQueue<Runnable> DbQueue = new LinkedBlockingQueue<Runnable>();
	/**
	 * 放入Map，和交换map的锁，防止刚放入然后被清空
	 */
	private ReentrantReadWriteLock getElementLock = new ReentrantReadWriteLock();
	
	private final ConcurrentHashMap<Class<?>, ConcurrentHashSet<?>> DB_OBJECT_MAP = new ConcurrentHashMap<Class<?>, ConcurrentHashSet<?>>();

	private <T> void handleTask(CallBack callBack,EntityCache<T> entityCache){
		if(callBack != null)
			callBack.callBefore();
		for (T t : entityCache.getEntities()) {
			try{
				System.out.println(t.toString() + "Now BE handled");
			}catch(Exception e){
				e.printStackTrace();
				if(entityCache.getRetryCount() <= 3){
					entityCache.setRetryCount(entityCache.getRetryCount()+1);
				}
			}
		}
		if(callBack != null){
			callBack.callAfter();
		}
	}
	
	/**
	 * 将MAP中的数据放入DBQUEUE，注意需要锁
	 */
	private void submitCache2Queue(){
		getElementLock.writeLock().lock();
		if (DB_OBJECT_MAP.isEmpty()) {
			return;
		}
		ConcurrentHashMap<Class<?>, ConcurrentHashSet<?>> tempCache = new ConcurrentHashMap<Class<?>, ConcurrentHashSet<?>>();
		try{
			tempCache.putAll(DB_OBJECT_MAP);
			DB_OBJECT_MAP.clear();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			getElementLock.writeLock().unlock();
		}
		for (ConcurrentHashSet<?> set : tempCache.values()) {
			EntityCache<Object> entityCache = new EntityCache<Object>(set);
			DbQueue.add(CreateDbTask(null, entityCache));
		}
	}
	
	private void updateInTime(){
		int subCount = 100;
		ConcurrentHashMap<Class<?>, ConcurrentHashSet<?>> tempCache = new ConcurrentHashMap<Class<?>, ConcurrentHashSet<?>>();
		getElementLock.writeLock().lock();
		try{
			tempCache.putAll(DB_OBJECT_MAP);
			DB_OBJECT_MAP.clear();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			getElementLock.writeLock().unlock();
		}
		//如果MAP中的数据很多，可以分散所有的MAP到不同的Queue中。防止单个QUEUE数量太多。
		for (ConcurrentHashSet<?> set : tempCache.values()) {
			ArrayList<Object> arrayList = new ArrayList<Object>(set);
			  int maxCount = set.size() % subCount == 0 ? set.size() / subCount :set.size()  / subCount + 1;
			for(int i = 0; i < maxCount; i++){
				DbQueue.add(CreateDbTask(null, new EntityCache<Object>(arrayList.subList(i * subCount, Math.min((i + 1) * subCount,set.size())))));
			}
		}
	}
	
	private class ShutDownHandler implements Runnable{
		public void run() {
			while(dbExecutor != null){
				setShutDown(true);
				if(dbExecutor.isShutdown())
					break;
				//在关闭的时候注意等待剩余的所有数据入库
				if(!DB_OBJECT_MAP.isEmpty()){
					updateInTime();
				}
				if(DbQueue.size() <= 0){
					dbExecutor.shutdown();
					try {
						//等待5分钟
						dbExecutor.awaitTermination(300, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	

	public void init(){
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHandler() {
		}));
		//可配置的线程池
		dbExecutor = new ThreadPoolExecutor(1,1,1000,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
		
		/**把放入从缓冲Map组装队列的操作和从实际组装队列的数据库操作进行分离*/
		/**分离的原因是两者可以并行操作，且一个是IO,一个是CPU操作互不在资源上有影响，且没有并发因素。
		否则会导致IO某次慢了,MAP屯了很多,导致IO下次可能更慢Map又一次屯了很多，影响越来越大。**/
		
		//单线程用户处理MAP交换，并组装到DBQUEUE的进程
		new Thread(new Runnable(){
				@Override
				public void run() {
					while(true){
						try{
							if(DB_OBJECT_MAP.size() == 0){
								try {
									Thread.sleep(WAIT_CACHE_SECOND);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							submitCache2Queue();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
		},"handleCacheMap").start();
		
		//用于组装好的DB Queue 来主动放入线程池的进程
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					if (DbQueue.size() == 0) {
						try{
							Thread.sleep(WAIT_CACHE_SECOND);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					try {
						Runnable task = DbQueue.take();
						if(task != null)
								dbExecutor.submit(task);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		},"executor").start();;
	}
	
	/**
	 * 创建DB处理对象
	 * @param callBack
	 * @param entityCache
	 * @return
	 */
	private <T> Runnable CreateDbTask(final CallBack callBack,final EntityCache<T> entityCache){
		return new Runnable(){
			@Override
			public void run() {
				 handleTask(callBack, entityCache);
			}
		};
	}
	
	
	/**
	 *  单个传入需要存储的entity,用锁锁住防止被读取的程序clear掉
	 * @param entity
	 */
	private <T> void put2ObjectMap(T entity){
		getElementLock.readLock().lock();;
		try{
			ConcurrentHashSet<T> hashSet = (ConcurrentHashSet<T>) DB_OBJECT_MAP.get(entity.getClass());
			if(hashSet == null){
				DB_OBJECT_MAP.putIfAbsent(entity.getClass(), new ConcurrentHashSet<T>());
				hashSet = (ConcurrentHashSet<T>) DB_OBJECT_MAP.get(entity.getClass());
			};
			hashSet.add(entity);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			getElementLock.readLock().unlock();
		}
		
	}
	
	/**
	 * 接口批量传入数据
	 * @param entities
	 */
	private <T> void put2ObjectMap(Collection<T> entities){
		for (T t : entities) {
			put2ObjectMap(t);
		}
	}
	
	public <T> void submitUpdate2Queue(T t){
		if(t == null)
			return;
		if(WAIT_CACHE_SECOND <= 0){
			CreateDbTask(null, new EntityCache<T>(t));
		}
		put2ObjectMap(t);
	}
	
	public <T> void submitUpdate2Queue(Collection<T> entities){
		if(entities.size() == 0)
			return;
		if(WAIT_CACHE_SECOND <= 0)
			CreateDbTask(null, new EntityCache<T>(entities));
		put2ObjectMap(entities);
	}

	/**
	 * Task 中要被处理的POJO队列，整个类的所有需要更新的对象放在一个队列里
	 * @author Gabriel
	 *
	 * @param <T>
	 */
	private class EntityCache<T>
	{	
		private Collection<T> entities;
		/**
		 * 重试次数
		 */
		private int retryCount;
		public EntityCache(Collection<T> entities){
			this.entities = new ArrayList<T>(entities);
		}
		public EntityCache(T t){
			setEntities(new ArrayList<T>());
			getEntities().add(t);
		}
		public Collection<T> getEntities() {
			return entities;
		}
		public void setEntities(Collection<T> entities) {
			this.entities = entities;
		}
		public int getRetryCount() {
			return retryCount;
		}
		public void setRetryCount(int retryCount) {
			this.retryCount = retryCount;
		}
	}
	
	public static void main(String[] args){
		new MemDbService().init();
	}

	public boolean isShutDown() {
		return shutDown;
	}

	public void setShutDown(boolean shutDown) {
		this.shutDown = shutDown;
	}
}
