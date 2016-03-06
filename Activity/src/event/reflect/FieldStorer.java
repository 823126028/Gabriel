package event.reflect;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import event.annotation.ParamName;
import event.comm.ChargeActivity;
import event.comm.Model;
import event.comm.PlayerEvent;

public class FieldStorer {
	private static Map<String,Field> PeFieldMap = new HashMap<String,Field>();
	private static Map<Integer,Class<?>> needFillSet = new HashMap<Integer,Class<?>>();
	private static Map<Class<?>,Integer> Class2Type = new HashMap<Class<?>,Integer>();
	
	private static Map<Class<?>,Map<String,Field>> translationFieldMap = new HashMap<Class<?>,Map<String,Field>> ();
	
	
	static{
		//活动1,充值活动
		needFillSet.put(1, ChargeActivity.class);
		Class2Type.put(ChargeActivity.class,1);
		
		for (Field field : PlayerEvent.class.getDeclaredFields()) {
			if(!Modifier.isStatic(field.getModifiers())){
				PeFieldMap.put(field.getName(), field);
			}
		}
		
		for (Class<?> clazz : needFillSet.values()) {
			Map<String,Field> paramMap = new HashMap<String,Field>();
			for (Field field : clazz.getDeclaredFields()) {
				ParamName paraName = field.getAnnotation(ParamName.class);
				if(paraName != null){
					paramMap.put(paraName.value(), field);
				}
			}
			translationFieldMap.put(clazz, paramMap);
		}
	}
	
	public static PlayerEvent modelToPe(Model model) throws InstantiationException, IllegalAccessException{
		if(model == null)
			return null;
		PlayerEvent pe = PlayerEvent.class.newInstance();
		Map<String,Field> paramMap = translationFieldMap.get(model.getClass());
		for (Entry<String, Field> entry : paramMap.entrySet()) {
			Field field = PeFieldMap.get(entry.getKey());
			field.set(pe, entry.getValue().get(model));
		}
		pe.setType(Class2Type.get(model.getClass()));
		return pe;
	}
	
	public static Model PeToModel(PlayerEvent pe) throws InstantiationException, IllegalAccessException{
		if(pe == null)
			return null;
		Class<?> clazz = needFillSet.get(pe.getType());
		Model model = (Model) clazz.newInstance();
		Map<String,Field> paramMap = translationFieldMap.get(clazz);
		
		for (Entry<String, Field> entry : PeFieldMap.entrySet()) {
			Field field = paramMap.get(entry.getKey());
			if(field == null)
				continue;
			field.set(model, entry.getValue().get(pe));
		}
		return model;
	}
	
	
}
