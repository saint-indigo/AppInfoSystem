package com.pmz.service.app_info;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmz.dao.app_info.app_infoMapper;
import com.pmz.pojo.app_category;
import com.pmz.pojo.app_info;
import com.pmz.pojo.app_version;
import com.pmz.pojo.data_dictionary;

@Service
@Transactional
public class app_infoServiceImpl implements app_infoService{

	@Resource
	private app_infoMapper app_infoMapper;

	@Override
	@Transactional(readOnly=true)
	public List<data_dictionary> type() throws Exception {
		List<data_dictionary> list =new ArrayList<data_dictionary>();
		list=app_infoMapper.type();
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<app_category> getList(Integer parentId) throws Exception {
		List<app_category> list =new ArrayList<app_category>();
		list=app_infoMapper.getList(parentId);
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<app_info> getAllList(String softwareName, Integer status,
			Integer flatformId, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3,Integer devId,int currentPageNo, int pageSize)
					throws Exception {
		List<app_info> list =new ArrayList<app_info>();
		currentPageNo=(currentPageNo-1)*pageSize;
		list=app_infoMapper.getAllList(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3,devId,currentPageNo, pageSize);
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public int getcount(String softwareName, Integer status,
			Integer flatformId, Integer categoryLevel1, Integer categoryLevel2,
			Integer categoryLevel3,Integer devId) throws Exception {
		int result=app_infoMapper.getcount(softwareName, status, flatformId, categoryLevel1, categoryLevel2, categoryLevel3,devId);
		return result;
	}

	@Override
	@Transactional(readOnly=true)
	public Integer APKName(String APKName) throws Exception {
		int result;
		if(app_infoMapper.APKName(APKName)>0){
			result=1;
		}else{
			result=0;
		}
		return result;
	}

	@Override
	public boolean addApp(app_info app) throws Exception {
		boolean index;

		int result=app_infoMapper.addApp(app);
		if(result>0){
			index=true;
		}else{
			index=false;
		}
		return index;
	}




	@Override
	public boolean updateApp(app_info app) throws Exception {
		boolean index;

		int result=app_infoMapper.updateApp(app);
		if(result>0){
			index=true;
		}else{
			index=false;
		}
		return index;
	}

	@Override
	@Transactional(readOnly=true)
	public app_info getOne(Integer id) throws Exception {
		app_info app=new app_info();
		app=app_infoMapper.getOne(id);
		return app;
	}

	@Override
	public boolean deleteApp(Integer id) throws Exception {
		boolean index;
		if(app_infoMapper.deleteApp(id)>0){
			index=true;
		}else{
			index=false;
		}
		return index;
	}

	@Override
	public boolean deleteLogo(Integer id) throws Exception {
		boolean index;
		if(app_infoMapper.deleteLogo(id)>0){
			index=true;
		}else{
			index=false;
		}
		return index;
	}

	@Override
	public boolean updateStatus(app_info app) throws Exception {
		boolean index=false;
		int result=app.getStatus();
		if(result==2||result==5){
			app.setStatus(4);
			if(app_infoMapper.updateApp(app)>0){
				index=true;
			}
		}
		if(result==4){
			app.setStatus(5);
			if(app_infoMapper.updateApp(app)>0){
				index=true;
			}
		}
		return index;
	}

	@Override
	public boolean checkStatus(app_info app) throws Exception {
		boolean index=false;
		if(app_infoMapper.updateApp(app)>0){
			index=true;
		}
		return index;
	}
	
	






}
