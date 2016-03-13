package SlabObj;

public abstract class SlabObject {
	public abstract	void init();
	public abstract void resert();
	private SlabObject pre;
	private SlabObject next;
	private int flag;
	
	public int getFlag(){
		return flag;
	}
	
	public void setFlag(int flag){
		this.flag = flag;
	}
	
	public SlabObject getPre() {
		return pre;
	}
	public void setPre(SlabObject pre) {
		this.pre = pre;
	}
	public SlabObject getNext() {
		return next;
	}
	public void setNext(SlabObject next) {
		this.next = next;
	}
}
