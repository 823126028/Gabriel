package event.comm;

import event.annotation.ParamName;

/**
 * @author Gabriel
 */
public class ChargeActivity extends Model{
	@ParamName("pid")
	public int pid;
	
	@ParamName("param1")
	public int chargeNum;
	
	@ParamName("param2")
	public int chargeTime;
	
	@ParamName("param3")
	public int leftTime;
	

	public int getChargeNum() {
		return chargeNum;
	}

	public void setChargeNum(int chargeNum) {
		this.chargeNum = chargeNum;
	}

	public int getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(int chargeTime) {
		this.chargeTime = chargeTime;
	}

	public int getLeftTime() {
		return leftTime;
	}

	public void setLeftTime(int leftTime) {
		this.leftTime = leftTime;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
}
