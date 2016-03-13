package com.project.invocate.injector;
import com.project.lang.LangUtil;
import com.project.net.Request;

public class KeyValueInjector extends ParamInjector{
	private String key;
	private Class<?> type;
	public void init(String key,Class<?> type){
		this.key = key;
		this.type = type;
	}
	public Object inject(Request request){
		 String o = request.getParamMap().get(key);
		 if(o == null){
			 return LangUtil.getDefaultValue(type);
		 }
		 return LangUtil.castTo(o, type);
	}
}
