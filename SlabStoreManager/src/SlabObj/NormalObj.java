package SlabObj;

import java.util.HashMap;
import java.util.LinkedList;

public class NormalObj extends SlabObject {
	private String key;
	private int value;
	private Object object;
	private HashMap<String,String> hashMap = new HashMap<String,String>(32);
	private HashMap<String,Integer> hashMap2 = new HashMap<String,Integer>(32);
	private HashMap<String,Integer> hashMap3 = new HashMap<String,Integer>(32);
	private HashMap<String,Integer> hashMap4 = new HashMap<String,Integer>(32);
	private LinkedList<String> linkList = new LinkedList<String>();
	private LinkedList<String> linkList2 = new LinkedList<String>();
	public void init1() {
		setKey("heihei");
		setValue(0);
	}

	public void resert1() {
		setKey("");
		setValue(0);		
	}

	public int getValue1() {
		return value;
	}

	public void setValue1(int value) {
		this.value = value;
	}

	public String getKey1() {
		return key;
	}

	public void setKey1(String key) {
		this.key = key;
	}
	
	@Override
	public void init() {
		setKey("heihei");
		setValue(0);
	}

	@Override
	public void resert() {
		setKey("");
		setValue(0);
		setFlag(0);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public HashMap<String,String> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String,String> hashMap) {
		this.hashMap = hashMap;
	}

	public HashMap<String,Integer> getHashMap2() {
		return hashMap2;
	}

	public void setHashMap2(HashMap<String,Integer> hashMap2) {
		this.hashMap2 = hashMap2;
	}

	public HashMap<String,Integer> getHashMap3() {
		return hashMap3;
	}

	public void setHashMap3(HashMap<String,Integer> hashMap3) {
		this.hashMap3 = hashMap3;
	}

	public HashMap<String,Integer> getHashMap4() {
		return hashMap4;
	}

	public void setHashMap4(HashMap<String,Integer> hashMap4) {
		this.hashMap4 = hashMap4;
	}

	public LinkedList<String> getLinkList() {
		return linkList;
	}

	public void setLinkList(LinkedList<String> linkList) {
		this.linkList = linkList;
	}

	public LinkedList<String> getLinkList2() {
		return linkList2;
	}

	public void setLinkList2(LinkedList<String> linkList2) {
		this.linkList2 = linkList2;
	}

}
