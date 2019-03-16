package com.pmz.service.backend_user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmz.dao.backend_user.backend_userMapper;
import com.pmz.dao.dev_user.dev_userMapper;
import com.pmz.pojo.backend_user;

@Service
@Transactional
public class backend_userServiceImpl implements backend_userService{

	@Resource
	private backend_userMapper backend_userMapper;
	
	@Override
	@Transactional(readOnly=true)
	public backend_user Login(String userCode, String userPassword)
			throws Exception {
		backend_user bu=new backend_user();
		bu=backend_userMapper.Login(userCode, userPassword);
		return bu;
	}

}
