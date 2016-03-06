package event.dao;
import event.comm.Model;

public interface IPlayerEventDao {

	void createPlayerEvent(Model pe);

	int updatePlayerEvent(Model pe);

	Model getByPIdAndType(int pid);

}
