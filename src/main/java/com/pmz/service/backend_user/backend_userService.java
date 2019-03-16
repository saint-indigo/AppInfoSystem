package com.pmz.service.backend_user;

import org.apache.ibatis.annotations.Param;

import com.pmz.pojo.backend_user;

public interface backend_userService {
	
	public backend_user Login(@Param("userCode")String userCode,@Param("userPassword")String userPassword)throws Exception; 


}
