package gabriel.consistent.hash.node;

import gabriel.consistent.hash.node.cache.INodeCache;
import gabriel.consistent.util.MD5Util;

import java.util.Date;

public class Node<T>{
	private String ip;
	private Date startTime;
	@SuppressWarnings("unused")
	private INodeCache<T> nodeCache;
	
	@SuppressWarnings("unchecked")
	public Node(String ip,String cacheComponent) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		nodeCache  = (INodeCache<T>) Thread.currentThread().getContextClassLoader().loadClass(cacheComponent).newInstance();
		setStartTime(new Date());
		this.ip = ip;
		nodeCache.init(ip);;
		
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String toHash(int sequence){
		return MD5Util.MD5(getIp() + "$" + sequence);
	}	
	
	@Override
	public boolean equals(Object node){
		return this.ip.equals(((Node)(node)).ip);
	}
	
	@Override
	public int hashCode(){
		return ip.hashCode();
	}
	
	public boolean addToCache(String key,T value){
		return nodeCache.putToCache(key, value);
	}
	
	public T getValueFromCache(String key){
		return nodeCache.getValue(key);
	}
}
