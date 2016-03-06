package event.dao;
import java.util.HashMap;
import java.util.Map;

import event.comm.Model;
import event.comm.PlayerEvent;
import event.reflect.FieldStorer;


public class PlayerEventDao implements IPlayerEventDao{
	
	/**模仿数据库*/
	Map<Integer,PlayerEvent> map = new HashMap<Integer,PlayerEvent>();
	
	/**从数据库里*/
	@Override
	public Model getByPIdAndType(int pid){
		PlayerEvent pe = map.get(pid);
		try {
			return FieldStorer.PeToModel(pe);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public void createPlayerEvent(Model model) {
		PlayerEvent pe = null;
		try {
			pe = FieldStorer.modelToPe(model);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		createPlayerEvent(pe);
	}
	
	@Override
	public int updatePlayerEvent(Model model) {
		PlayerEvent pe = null;
		try {
			pe = FieldStorer.modelToPe(model);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return updatePlayerEvent(pe);
	}
	
	private void createPlayerEvent(PlayerEvent pe){
		map.put(pe.getPid(), pe);
	}
	
	private int updatePlayerEvent(PlayerEvent pe){
		return map.put(pe.getPid(), pe) != null ? 1:0;
	}
	
}
