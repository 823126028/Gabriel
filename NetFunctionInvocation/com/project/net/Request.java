package com.project.net;

import java.util.Map;

public class Request {
	private Map<String,String> paramMap;
	private String command;

	public Map<String,String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String,String> paramMap) {
		this.paramMap = paramMap;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
