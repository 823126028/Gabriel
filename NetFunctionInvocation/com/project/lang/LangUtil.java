package com.project.lang;

import java.lang.annotation.Annotation;


public class LangUtil {
	public static Object getDefaultValue(Class<?> type){
		if(type == int.class){
			return 0;
		}
		if(type == Integer.class){
			return 0;
		}
		if(type == String.class){
			return null;
		}
		return null;
	}
	
	public static Object castTo(String src,Class<?> type){
		if(type == int.class || type == Integer.class){
			return Integer.parseInt(src);
		}
		if(type == String.class)
			return src;
		return null;
	}
	
	public static <T extends Annotation> T getAnnotation(Class<?> clazz,Class<T> annotation){
		Class<?> opClazz = clazz;
		T result = null;
		while(opClazz != null && opClazz != Object.class){
			result = opClazz.getAnnotation(annotation);
			if(result == null){
				opClazz = opClazz.getSuperclass();
			}else{
				return result;
			}
		}
		return result;
	}
	
	public static Object getInstanceOfClazz(Class<?> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	
}
