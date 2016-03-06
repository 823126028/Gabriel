项目用于将游戏中活动相关的数据库对象转为相应实体对象在业务逻辑中进行使用。

通常活动的数据库存储格式为:
pid,
type,
param1,
param2,
.....

通过注解反射,可以将数据库存储和实际对象进行转化使业务逻辑更清楚
ChargeActivity
@paramName("param1")
int chargeTime
@paramName("param2")
int chargeNum


event.annotation.ParamName
	@ParamName 标记所有需要改变的注解名

event.comm.Model
	所有需要转化的活动必须继承Model，来确定它是活动逻辑类

event.reflect.FieldStorer
	用来缓存反射的field来提高效率,并且提供两者互相转化的接口

	/** 将逻辑Entity转化为数据库Entity*/
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
	
	/**将数据库Entity根据类型转化成不同的逻辑Actity*/
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

event.dao.PlayerEventDao，用一个HashMap来模拟数据库，并提供数据库接口

TestCase:提供的测试用例

event.domain.PlayerEvent,数据库存储实体

event.comm.Model 所有逻辑实体的父类

event.comm.ChargeActivity 逻辑实体