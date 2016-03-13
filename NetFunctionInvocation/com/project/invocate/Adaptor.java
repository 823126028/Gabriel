package com.project.invocate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import com.project.annotation.RequestParam;

public class Adaptor {
	private ParamInjector[] paramInjector;
	/**初始化参数集合*/
	public void init(Method method){
		Class<?>[] types = method.getParameterTypes();
		paramInjector = new ParamInjector[types.length];
		Annotation[][] annotations = method.getParameterAnnotations();
		for(int i = 0; i < annotations.length; i++){
			for (int j = 0; j < annotations[i].length; j++) {
				if(annotations[i][j] instanceof RequestParam){
					paramInjector[i] = new ParamInjector();
					paramInjector[i].enjectParam(((RequestParam)annotations[i][j]).value() , types[i]);
					//如果找到参数,那么直接返回
					break;
				}
			}
		}
	}
	
	public Object[] adapt(Map<String,String> paramMap){
		Object[] objArray = new Object[paramInjector.length];
		for (int i = 0; i < paramInjector.length; i++) {
			objArray[i] = paramInjector[i].dejectParam(paramMap);
		}
		return objArray;
	}
}
