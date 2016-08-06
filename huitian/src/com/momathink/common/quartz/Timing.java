package com.momathink.common.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.momathink.common.base.Util;

public class Timing implements ServletContextListener {
	Timer courseTimer;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		courseTimer.cancel();// 定时退出
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			if ("on".equalsIgnoreCase(Util.getPropVal("smsStatus"))) {
				int hour = 0, minute = 0;
				String smsHour = Util.getPropVal("smsHour");
				String smsMinute = Util.getPropVal("smsMinute");
				if (smsHour != null && !"".equals(smsHour)) {
					hour = Integer.parseInt(smsHour);
				}
				if (smsMinute != null && !"".equals(smsMinute)) {
					minute = Integer.parseInt(smsMinute);
				}
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, hour); // 控制时
				calendar.set(Calendar.MINUTE, minute); // 控制分
				calendar.set(Calendar.SECOND, 59); // 控制秒
				Date time = calendar.getTime(); // 得出执行任务的时间
				courseTimer = new Timer();
				courseTimer.schedule(new Task(courseTimer), time, 1000 * 60 * 60 * 24);// 执行定时发送课程信息
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
