package com.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.project.interceptor.Interceptor;
import com.project.invocate.InvocationManager;
import com.project.net.Request;
import com.project.scan.ScanUtil;

public class ScanInfo {
	public static void main(String[] args){
		HashSet<Class<?>> set = ScanUtil.getClasses("com.project.action");
		List<Interceptor> arrayList = new ArrayList<Interceptor>();
		InvocationManager.SearchInvocation(set, arrayList);
		Request request = new Request();
		request.setCommand("test@print");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("num", "10");
		request.setParamMap(paramMap);
		InvocationManager.ServiceRequest(request);
		
		Request request2 = new Request();
		Map<String,String> paramMap2 = new HashMap<String,String>();
		paramMap2.put("num", "30");
		request2.setParamMap(paramMap2);
		request2.setCommand("test@print2");
		InvocationManager.ServiceRequest(request2);
	}
}
