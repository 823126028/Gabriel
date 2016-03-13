package com.project.interceptor;

import java.util.Iterator;

import com.project.interceptor.Interceptor;
import com.project.invocate.Invocation;
import com.project.net.Request;

public class InterceptorTestOne extends Interceptor{
	@Override
	public void interceptor(Invocation i, Iterator<Interceptor> incepterIterator,Request request) {
		System.out.println("InterceptorTestOne---------------");
		i.__invoke(incepterIterator, request);
		
	}

}
 