package gabriel.consistent.hash;

import gabriel.consistent.hash.node.Configuration;
import gabriel.consistent.hash.node.VirtualNode;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import gabriel.consistent.hash.node.Node;
import gabriel.consistent.util.MD5Util;

public class VirtualNodeManager<T> {
	private TreeMap<String,VirtualNode<T>> virtualMap = new TreeMap<String,VirtualNode<T>>();
	private Set<Node<T>> realSet = new HashSet<Node<T>>();
	
	private VirtualNode<T> chooseVNode(String md5Key){
		if(virtualMap.containsKey(md5Key)){
			return virtualMap.get(md5Key);
		}
		SortedMap<String,VirtualNode<T>> sMap = virtualMap.tailMap(md5Key);
		if(sMap != null && !sMap.isEmpty()){
			return sMap.get(sMap.firstKey());
		}
		return virtualMap.firstEntry().getValue();
	}
	
	public boolean putValue(String key,T value){
		String md5Key = MD5Util.MD5(key);
		VirtualNode<T> vnode = chooseVNode(md5Key);
		System.out.println("======================put:" + key);
		vnode.printVnodeInfo();
		System.out.println("======================put");
		return vnode.addToCache(key, value);
	}
	
	public T getValueFromCache(String key){
		String md5Key = MD5Util.MD5(key);
		VirtualNode<T> vnode = chooseVNode(md5Key);
		System.out.println("======================get:" + key);
		vnode.printVnodeInfo();
		System.out.println("======================get");
		return vnode.getValueFromCache(key);
	}
	
	
	public boolean addNodeToMananger(Node<T> node){
		if(realSet.add(node)){
			int replicateNums = Configuration.replicatVNums;
			for(int i = 1; i <= replicateNums; i++){
				VirtualNode<T> vNode = new VirtualNode<T>(node, i);
				virtualMap.put(node.toHash(i), vNode);
			}
			return true;
		}
		return false;
	}
	
	public boolean removeNodeFromManager(Node<T> node){
		if(realSet.remove(node)){
			int replicateNums = Configuration.replicatVNums;
			for(int i = 1; i <= replicateNums; i++){
				virtualMap.remove(node.toHash(i));
			}
			return true;
		}
		return false;
	}
	
	
}
