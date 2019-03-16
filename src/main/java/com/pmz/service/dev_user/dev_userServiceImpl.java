package com.pmz.service.dev_user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.pmz.dao.dev_user.dev_userMapper;
import com.pmz.pojo.dev_user;

@Service
@Transactional
public class dev_userServiceImpl implements dev_userService{

	@Resource
	private dev_userMapper dev_userMapper;
	
	@Override
	@Transactional(readOnly=true)
	public dev_user Login(String devCode, String devPassword) throws Exception {
		// TODO Auto-generated method stub
		dev_user user=new dev_user();
		user=dev_userMapper.Login(devCode, devPassword);	
		return user;
	}

}
