package com.asynQueue;

import java.util.concurrent.ConcurrentHashMap;
import com.asynQueue.QueueExecutor;
import com.asynQueue.QueueRunner;


/**
 * 玩家异步资源增加管理类
 * @author Gabriel
 *
 */
public class PlayerAsyResourceManager {
	private static final PlayerAsyResourceManager __instance = new PlayerAsyResourceManager();
	private static final QueueExecutor queueExecutor = new QueueExecutor();
	static{
		queueExecutor.init();
	}
	
	/**playerId_resourceType  199_iron资源和铁) */
	private ConcurrentHashMap<String,QueueRunner> resourceMap = new ConcurrentHashMap<String,QueueRunner>();
	
	private PlayerAsyResourceManager(){};
	
	public void shutDown(){
		queueExecutor.shutDown();
	}
	
	public static PlayerAsyResourceManager getInstance(){
		return __instance;
	}
	
	/**
	 * 通过key无锁获得QueueRunner,没有则创建
	 * @param key
	 * @return
	 */
	public QueueRunner getQueueRunnerByKey(String key){
		QueueRunner qr =  resourceMap.get(key);
		if(qr == null){
			qr  = new QueueRunner(queueExecutor,key);
			QueueRunner old = resourceMap.putIfAbsent(key, qr);
			if(old != null){
				qr = old;
			}
		}
		return qr;
	}

}
