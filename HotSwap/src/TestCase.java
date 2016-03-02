public class TestCase {
	public static void print(){
		System.out.println("---------");
	}
	public static void main(String[] args){
		while(true){
			try {
				Thread.sleep(5000);
				print();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
