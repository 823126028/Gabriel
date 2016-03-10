package com.project.interceptor;

import java.util.Iterator;

import com.project.invocate.Invocation;
import com.project.net.Request;

public abstract class Interceptor {
	public abstract void interceptor(Invocation i, Iterator<Interceptor> incepterIterator,Request request);
}
