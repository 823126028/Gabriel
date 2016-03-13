package com.project.invocate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.project.annotation.RequestParam;
import com.project.invocate.injector.KeyValueInjector;
import com.project.invocate.injector.ParamInjector;
import com.project.net.Request;

public class Adaptor {
	private ParamInjector[] paramInjector;
	public void init(Method method){
		Class<?>[] types = method.getParameterTypes();
		paramInjector = new ParamInjector[types.length];
		Annotation[][] annotations = method.getParameterAnnotations();
		for(int i = 0; i < annotations.length; i++){
			for (int j = 0; j < annotations[i].length; j++) {
				if(annotations[i][j] instanceof RequestParam){
					KeyValueInjector injector = new KeyValueInjector();
					paramInjector[i] = injector;
					injector.init(((RequestParam)annotations[i][j]).value() , types[i]);
				}
			}
		}
	}
	
	public Object[] adapt(Request reqeust){
		Object[] objArray = new Object[paramInjector.length];
		for (int i = 0; i < paramInjector.length; i++) {
			objArray[i] = paramInjector[i].inject(reqeust);
		}
		return objArray;
	}
}
