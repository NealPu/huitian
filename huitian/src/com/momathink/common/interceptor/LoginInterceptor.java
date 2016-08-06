package com.momathink.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * BlogInterceptor
 */
public class LoginInterceptor implements Interceptor {
	//private static Logger log = Logger.getLogger(LoginInterceptor.class);
	public void intercept(Invocation ai) {
		try {
			String actionKey = ai.getActionKey();
			Object account = ai.getController().getSessionAttr("account_session");
			if (account != null 
					|| ("/account/login").equals(actionKey)
					|| ("/account/doLogin").equals(actionKey)
					|| (actionKey).contains("getStudentForWordSys")
					|| (actionKey).contains("queryCoursePlansForToday")
					|| (actionKey).contains("queryCoursePlansForZGC")
					|| (actionKey).contains("/api/")
					|| (actionKey).contains("/weixin/message")
					|| (actionKey).contains("/weixin/mediator")
					|| (actionKey).contains("queryStudentForTeacher")
					|| (actionKey).contains("allCourseplanToday")
					|| (actionKey).contains("/student/toSaveStudentGrade")
					|| (actionKey).contains("/student/assessCourse")) {
				ai.invoke();
			} else {
				// System.out.println("Before invoking " + ai.getActionKey());
				ai.getController().redirect("/account/login");
				// System.out.println("After invoking " + ai.getActionKey());

			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
