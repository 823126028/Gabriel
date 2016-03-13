package com.project.invocate.injector;

import com.project.net.Request;

public abstract class ParamInjector {
	public abstract Object inject(Request request);
}
