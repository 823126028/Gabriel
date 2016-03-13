package com.project.interceptor;

import java.util.Iterator;

import com.project.interceptor.Interceptor;
import com.project.invocate.Invocation;
import com.project.net.Request;

public class InterceptorTest2 extends Interceptor{
	@Override
	public void interceptor(Invocation i, Iterator<Interceptor> incepterIterator,Request request) {
		System.out.println("InterceptorTestTwo---------------");
		i.__invoke(incepterIterator, request);
		
	}
}
 