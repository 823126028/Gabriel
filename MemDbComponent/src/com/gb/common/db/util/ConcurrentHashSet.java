package com.gb.common.db.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<T> extends AbstractSet<T> implements Set<T>{
	
	private ConcurrentHashMap<T,Boolean> hashMap = new ConcurrentHashMap<T,Boolean>();
	@Override
	public Iterator<T> iterator() {
		return hashMap.keySet().iterator();
	}

	@Override
	public int size() {
		return hashMap.size();
	}
	
	public boolean isEmpty(){
		return hashMap.isEmpty();
	}
	
	public void clear(){
		hashMap.clear();
	}
	
	public boolean add(T o){
		hashMap.put(o, true);
		return true;
	}
	
	public boolean remove(Object o){
		return hashMap.remove(o);
	}
	
}
