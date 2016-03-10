package com.project.invocate;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.project.annotation.Action;
import com.project.annotation.Command;
import com.project.interceptor.Interceptor;
import com.project.lang.LangUtil;
import com.project.net.Request;

public class InvocationManager {
	/**
	 * 存放方法执行者的map
	 */
	public static Map<String,Invocation> iovacationMap = new HashMap<String,Invocation>();
	
	
	/**
	 * 在扫出的类里筛选注解(@Action为接口类)而接口类中的方法(@Command为方法)
	 * @param classSet
	 * @param incepters
	 */
	public static void  SearchInvocation(HashSet<Class<?>> classSet,List<Interceptor> incepters){
		for (Class<?> clazz : classSet) {
			//虚类,接口不要
			if(Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers()))
				continue;
			if(LangUtil.getAnnotation(clazz, Action.class) == null)
				continue;
			Object object = LangUtil.getInstanceOfClazz(clazz);
			
			for (Method method : clazz.getDeclaredMethods()) {
				if(Modifier.isAbstract(method.getModifiers()) || Modifier.isStatic(method.getModifiers()))
					continue;
				Command command = method.getAnnotation(Command.class);
				if(command != null){
					iovacationMap.put(command.value(), CreateInvocation(method, object,incepters)) ;
				}	
			}
		}
	}
	
	
	/**
	 * 解析request中的方法名和参数
	 * @param request
	 */
	public static void ServiceRequest(Request request){
		String command = request.getCommand();
		if(command == null || command.trim() == "")
			return;
		Invocation invoker = iovacationMap.get(command);
		if(invoker == null)
			return;
		invoker.doInvoke(request);
		
	}
	
	public static Invocation CreateInvocation(Method method,Object object,List<Interceptor> incepters){
		return new Invocation(method, object, incepters);
	}
}
