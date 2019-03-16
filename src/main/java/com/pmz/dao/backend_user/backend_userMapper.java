package com.pmz.dao.backend_user;

import org.apache.ibatis.annotations.Param;

import com.pmz.pojo.backend_user;


public interface backend_userMapper {
	//开发者登录
		public backend_user Login(@Param("userCode")String userCode,@Param("userPassword")String userPassword)throws Exception; 

}
