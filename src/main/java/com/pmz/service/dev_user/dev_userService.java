package com.pmz.service.dev_user;

import org.apache.ibatis.annotations.Param;

import com.pmz.pojo.dev_user;

public interface dev_userService {
	public dev_user Login(@Param("devCode")String devCode,@Param("devPassword")String devPassword)throws Exception;


}
