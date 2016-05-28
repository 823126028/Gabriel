package gabriel;

import gabriel.consistent.hash.VirtualNodeManager;
import gabriel.consistent.hash.node.Node;

public class TestCases {
	public static void main(String[] args){
		String className = "gabriel.consistent.hash.node.cache.LocalNodeCache";
		VirtualNodeManager<String> nodeManager = new VirtualNodeManager<String>();
		for(int i = 1; i <= 5; i++ ){
			try {
				nodeManager.addNodeToMananger(new Node<String>("127.0.0.1:" + 10 * i,className));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		nodeManager.putValue("test1key", "test1Value");
		nodeManager.putValue("test2key", "test2Value");
		nodeManager.putValue("test3key", "test3Value");
		nodeManager.putValue("test4key", "test4Value");
		nodeManager.putValue("test5key", "test5Value");
		nodeManager.putValue("test6key", "test6Value");
		nodeManager.putValue("testa111119key", "testaValue");
		try {
			nodeManager.removeNodeFromManager(new Node<String>("127.0.0.1:" +"" + 20,className));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(nodeManager.getValueFromCache("test1key"));
	}
}
