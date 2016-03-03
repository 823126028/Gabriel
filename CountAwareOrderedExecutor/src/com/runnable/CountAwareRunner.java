package com.runnable;
/**
 * 
 * 可执行task，以key作为不同类型的task的区分,run仅用于测试
 * @author Gabriel
 *
 */
public class CountAwareRunner implements Runnable{
	private String key;
	public CountAwareRunner(String key){
		this.key = key;
	}
	public String getKey(){
		return key;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(key + "==========");
	}
}
