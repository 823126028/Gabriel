import event.comm.ChargeActivity;
import event.dao.IPlayerEventDao;
import event.dao.PlayerEventDao;


public class TestCase {
	/**
	 * @param args
	 */
	public static void main(String[] args){
		ChargeActivity ca = new ChargeActivity();
		ca.setPid(1);
		ca.setChargeNum(10);
		ca.setChargeTime(10);
		ca.setLeftTime(5);
		IPlayerEventDao playerEventDao = new PlayerEventDao();
		playerEventDao.createPlayerEvent(ca);
		ca = (ChargeActivity) playerEventDao.getByPIdAndType(1);
	}
}
