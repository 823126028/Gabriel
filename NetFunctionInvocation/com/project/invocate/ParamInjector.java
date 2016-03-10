package com.project.invocate;
import java.util.Map;

import com.project.lang.LangUtil;

public class ParamInjector {
	private String key;
	private Class<?> type;
	public void enjectParam(String key,Class<?> type){
		this.key = key;
		this.type = type;
	}
	public Object dejectParam(Map<String,String> paramMap){
		 String o = paramMap.get(key);
		 if(o == null){
			 return LangUtil.getDefaultValue(type);
		 }
		 return LangUtil.castTo(o, type);
	}
}
