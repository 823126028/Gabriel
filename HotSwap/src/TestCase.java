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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
}
