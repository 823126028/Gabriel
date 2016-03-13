package com.project.invocate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import com.project.interceptor.Interceptor;
import com.project.net.Request;

public class Invocation {
	private Method method;
	private Adaptor adaptor;
	private List<Interceptor> incepterList;
	private Object invokerObj;
	//À¹½ØÆ÷==============
	
	
	public Invocation(Method method,Object invokerObj,List<Interceptor> incepterList){
		this.method = method;
		this.invokerObj = invokerObj;
		this.incepterList = incepterList;
		adaptor = new Adaptor();
		init();
	}
	
	public void init(){
		adaptor.init(method);
	}
	
	public void doInvoke(Request request){
		Iterator<Interceptor> incepterIterator = incepterList.iterator();
		__invoke(incepterIterator, request);
		
	}
	
	public void __invoke(Iterator<Interceptor> incepterIterator,Request request){
		if(incepterIterator != null && incepterIterator.hasNext()){
			Interceptor incepter = incepterIterator.next();
			incepter.interceptor(this, incepterIterator,request);
		}else{
			try {
				method.invoke(invokerObj, adaptor.adapt(request));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

}
