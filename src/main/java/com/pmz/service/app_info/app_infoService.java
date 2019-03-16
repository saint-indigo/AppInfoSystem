package com.pmz.service.app_info;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pmz.pojo.app_category;
import com.pmz.pojo.app_info;
import com.pmz.pojo.app_version;
import com.pmz.pojo.data_dictionary;

public interface app_infoService {
	public List<data_dictionary> type()throws Exception;
	
	public List<app_category> getList(@Param("parentId") Integer parentId)throws Exception;

	public List<app_info> getAllList(@Param("softwareName")String softwareName,@Param("status")Integer status,@Param("flatformId")Integer flatformId,@Param("categoryLevel1")Integer categoryLevel1,@Param("categoryLevel2")Integer categoryLevel2,@Param("categoryLevel3")Integer categoryLevel3,@Param("devId")Integer devId,@Param("currentPageNo")int currentPageNo, @Param("pageSize")int pageSize)throws Exception;

	public int getcount(@Param("softwareName")String softwareName,@Param("status")Integer status,@Param("flatformId")Integer flatformId,@Param("categoryLevel1")Integer categoryLevel1,@Param("categoryLevel2")Integer categoryLevel2,@Param("categoryLevel3")Integer categoryLevel3,@Param("devId")Integer devId)throws Exception;

	public Integer APKName(@Param("APKName")String APKName)throws Exception;
	
	public boolean addApp(app_info app)throws Exception;
		
	public boolean updateApp(app_info app)throws Exception;
	
	public app_info getOne(@Param("id")Integer id)throws Exception;
	
	public boolean deleteApp(@Param("id")Integer id)throws Exception;
	
	public boolean deleteLogo(@Param("id")Integer id)throws Exception;
	
	public boolean updateStatus(app_info app)throws Exception;
	
	public boolean checkStatus(app_info app)throws Exception;









}
