package com.momathink.teaching.subject.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.momathink.common.base.BaseService;
import com.momathink.teaching.subject.model.Subject;

public class SubjectService extends BaseService {
	private static Logger log = Logger.getLogger(SubjectService.class);
	public static final Subject dao = new Subject();
	public List<Subject> findAvailableSubject() {
		log.info("查询有效科目");
		return dao.findSubjectByState(0);
	}

}
