package com.project.intercptor;

import java.util.Iterator;

import com.project.interceptor.Interceptor;
import com.project.invocate.Invocation;
import com.project.net.Request;

public class IncepterPrint_2 extends Interceptor{
	@Override
	public void interceptor(Invocation i, Iterator<Interceptor> incepterIterator,Request request) {
		System.out.println("IncepterPrint_1---------------");
		i.__invoke(incepterIterator, request);
		
	}
}
 