package com.pmz.service.app_version;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmz.dao.app_version.app_versionMapper;
import com.pmz.pojo.app_version;
@Service
@Transactional
public class app_versionServiceImpl implements app_versionService{

	@Resource
	private app_versionMapper app_versionMapper;
	
	@Override
	@Transactional(readOnly=true)
	public List<app_version> getVersionList(Integer id) throws Exception {
		List<app_version> list=new ArrayList<app_version>();
		
		list=app_versionMapper.getVersionList(id);
		
		return list;
	}

	@Override
	public boolean addVersion(app_version verson) throws Exception {
		boolean index;
		int result=app_versionMapper.addVersion(verson);
		if(result>0){
                index=true;
		}else{
			    index=false;
		}
		return index;
	}

	@Override
	@Transactional(readOnly=true)
	public app_version getOne(Integer id) throws Exception {
		app_version ve=new app_version();
		ve=app_versionMapper.getOne(id);
		return ve;
	}

	@Override
	public boolean updateversion(app_version verson) throws Exception {
		boolean index;
		int result=app_versionMapper.updateversion(verson);
		if(result>0){
                index=true;
		}else{
			    index=false;
		}
		return index;
	}

	@Override
	public boolean deleteAPK(app_version version) throws Exception {
		boolean index;
		int result=app_versionMapper.deleteAPK(version);
		if(result>0){
                index=true;
		}else{
			    index=false;
		}
		return index;
	}

}
