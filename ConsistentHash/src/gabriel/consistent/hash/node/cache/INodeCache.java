package gabriel.consistent.hash.node.cache;

public interface INodeCache <T> {
	public void init(String param);
	public T getValue(String key);
	public boolean putToCache(String key,T value);
}
