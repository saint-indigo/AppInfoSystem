package com.pmz.dao.app_version;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pmz.pojo.app_version;

public interface app_versionMapper {
	public List<app_version> getVersionList(@Param("id")Integer id)throws Exception;
	
	public int addVersion(app_version verson)throws Exception;
	
	public app_version getOne(@Param("id")Integer id)throws Exception;
	
	public int updateversion(app_version verson)throws Exception;
	
	public int deleteAPK(app_version version)throws Exception;
	

}
