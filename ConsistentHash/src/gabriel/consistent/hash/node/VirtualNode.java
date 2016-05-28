package gabriel.consistent.hash.node;

public class VirtualNode<T> {
	private Node<T> realNode;
	private int sequence;

	
	public VirtualNode(Node<T> realNode,int sequence){		
		this.realNode = realNode;
		this.setSequence(sequence);
	}
	
	public boolean addToCache(String key,T value){
		return realNode.addToCache(key, value);
	}
	
	public T getValueFromCache(String key){
		return realNode.getValueFromCache(key);
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public void printVnodeInfo(){
		System.out.println(realNode.getIp() + "#" +sequence);
	}
}
