
/**动更的测试类仅用于测试*/
public class TestCase {
	public static void print(){
		System.out.println("hotswap test");
	}
	public static void main(String[] args){
		while(true){
			try {
				Thread.sleep(5000);
				print();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
