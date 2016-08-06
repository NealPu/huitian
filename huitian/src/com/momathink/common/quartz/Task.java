package com.momathink.common.quartz;

import java.util.Timer;

import com.momathink.sys.sms.controller.SendController;

public class Task extends  java.util.TimerTask{
	Timer timer ;
	SendController sendController=new SendController();
	public Task(Timer data){
		timer=data;
	}
	public void run() {
		sendController.sendCourseInfo();
		//timer.cancel();
	}

}
