package com.pmz.dao.dev_user;

import org.apache.ibatis.annotations.Param;

import com.pmz.pojo.dev_user;

public interface dev_userMapper {
	
	//开发者登录
	public dev_user Login(@Param("devCode")String devCode,@Param("devPassword")String devPassword)throws Exception; 

}
