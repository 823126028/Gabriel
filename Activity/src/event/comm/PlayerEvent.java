package event.comm;

public class PlayerEvent{
	public int pid;
	public int param1;
	public int param2;
	public int param3;
	public int param4;
	public int type;
	
	public int getParam1() {
		return param1;
	}
	public void setParam1(int param1) {
		this.param1 = param1;
	}
	public int getParam2() {
		return param2;
	}
	public void setParam2(int param2) {
		this.param2 = param2;
	}
	public int getParam3() {
		return param3;
	}
	public void setParam3(int param3) {
		this.param3 = param3;
	}
	public int getParam4() {
		return param4;
	}
	public void setParam4(int param4) {
		this.param4 = param4;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
