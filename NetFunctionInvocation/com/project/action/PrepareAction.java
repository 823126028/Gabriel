package com.project.action;

import com.project.annotation.Action;
import com.project.annotation.Command;
import com.project.annotation.RequestParam;

@Action
public class PrepareAction {
	@Command("test@print")
	public void print(@RequestParam("num")int num){
		System.out.println("1.====" + num);
	}
	@Command("test@print2")
	public void print2(@RequestParam("num")int num){
		System.out.println("2.=====" + num);
	}
}
