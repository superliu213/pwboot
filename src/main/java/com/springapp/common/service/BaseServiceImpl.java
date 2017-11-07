package com.springapp.common.service;

import com.springapp.common.busilog.service.SyncLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service 基类实现
 *
 */
public abstract class BaseServiceImpl {

	@Autowired
	protected SyncLogServiceImpl dbLog;

	protected Logger logger = LoggerFactory.getLogger(getClass());

}
