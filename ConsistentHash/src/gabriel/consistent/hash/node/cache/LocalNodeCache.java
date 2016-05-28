package gabriel.consistent.hash.node.cache;

import java.util.HashMap;

public class LocalNodeCache<T> implements INodeCache<T> {
	private HashMap<String,T> map = new HashMap<String,T>();
	
	@Override
	public void init(String param) {
		map = new HashMap<String,T>();
	}

	@Override
	public T getValue(String key) {
		return map.get(key);
	}

	@Override
	public boolean putToCache(String key,T value) {
		map.put(key, value);
		return true;
	}

}
