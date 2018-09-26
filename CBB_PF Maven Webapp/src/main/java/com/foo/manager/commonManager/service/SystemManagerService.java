package com.foo.manager.commonManager.service;

import javax.annotation.Resource;

import com.foo.abstractService.AbstractService;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.dao.mysql.NJCommonManagerMapper;

/**
 * @author xuxiaojun
 *
 */
public abstract class SystemManagerService extends AbstractService {
	@Resource
	protected CommonManagerMapper commonManagerMapper;
	@Resource
	protected NJCommonManagerMapper njCommonManagerMapper;
	
}
