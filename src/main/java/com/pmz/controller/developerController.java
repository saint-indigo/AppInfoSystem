package com.pmz.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.pmz.pojo.app_category;
import com.pmz.pojo.app_info;
import com.pmz.pojo.app_version;
import com.pmz.pojo.data_dictionary;
import com.pmz.pojo.dev_user;
import com.pmz.service.app_info.app_infoService;
import com.pmz.service.app_version.app_versionService;
import com.pmz.service.dev_user.dev_userService;
import com.pmz.tools.BaseConvert;
import com.pmz.tools.Constants;
import com.pmz.tools.PageSupport;
import com.sun.org.apache.regexp.internal.recompile;

import sun.misc.Hashing;

@Controller
@RequestMapping("/developer")
public class developerController extends BaseConvert{
	private Logger logger=Logger.getLogger(developerController.class);

	@Resource app_infoService app_infoService;
	@Resource app_versionService app_versionService;



	//APP列表--APP状态--所属平台--1分类
	@RequestMapping(value="/gettype.html")
	public String gettype(app_info app,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) String queryStatus,
			@RequestParam(value="queryFlatformId",required=false) String queryFlatformId,
			@RequestParam(value="queryCategoryLevel1",required=false) String queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) String queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) String queryCategoryLevel3,
			@RequestParam(value="pageIndex",required=false) String pageIndex,
			Model model,
			HttpSession session)throws Exception{

		dev_user user=(dev_user)session.getAttribute("devuser");
		int devId=user.getId();

		app.setDevId(devId);

		if(querySoftwareName!=null&&querySoftwareName!=""){
			app.setSoftwareName(querySoftwareName);
		}

		if(queryStatus!=null&&queryStatus!=""){
			app.setStatus(Integer.parseInt(queryStatus));
		}

		if(queryFlatformId!=null&&queryFlatformId!=""){
			app.setFlatformId(Integer.parseInt(queryFlatformId));
		}

		if(queryCategoryLevel1!=null&&queryCategoryLevel1!=""){
			app.setCategoryLevel1(Integer.parseInt(queryCategoryLevel1));
		}

		if(queryCategoryLevel2!=null&&queryCategoryLevel2!=""){
			app.setCategoryLevel2(Integer.parseInt(queryCategoryLevel2));
		}

		if(queryCategoryLevel3!=null&&queryCategoryLevel3!=""){
			app.setCategoryLevel3(Integer.parseInt(queryCategoryLevel3));
		}



		//平台--状态
		List<data_dictionary> list1=new ArrayList<data_dictionary>();
		list1=app_infoService.type();
		model.addAttribute("typelist",list1);
		//1级分类
		List<app_category> list2=new ArrayList<app_category>();
		list2=app_infoService.getList(null);
		model.addAttribute("Firstlist", list2);
		//APPINFO
		List<app_info> list3=null;

		//设置页面容量
		int pageSize=Constants.pageSize;
		//当前页码
		int currentPageNo=1;

		if(pageIndex!=null){
			try {
				currentPageNo=Integer.valueOf(pageIndex);	
			} catch (NumberFormatException e) {
				return "error";
			}
		}

		//总数量(表)
		int totalCount=app_infoService.getcount(app.getSoftwareName(), app.getStatus(), app.getFlatformId(), app.getCategoryLevel1(), app.getCategoryLevel2(), app.getCategoryLevel3(),app.getDevId());

		//总页数
		PageSupport pages=new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount=pages.getTotalPageCount();

		//控制首页和尾页
		if(currentPageNo<1){
			currentPageNo=totalPageCount;
		}

		list3=app_infoService.getAllList(app.getSoftwareName(), app.getStatus(), app.getFlatformId(), app.getCategoryLevel1(), app.getCategoryLevel2(), app.getCategoryLevel3(),app.getDevId(),currentPageNo, pageSize);
		model.addAttribute("AppList", list3);
		model.addAttribute("totalPageCount",totalPageCount);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("currentPageNo",currentPageNo);

		return "developer/applist";
	}



	//App123分类
	@RequestMapping(value="/gettt.json",method=RequestMethod.GET)
	@ResponseBody
	public List<app_category> gettt(@RequestParam("pid")Integer pid, Model model)throws Exception{
		List<app_category> list=new ArrayList<app_category>();
		list=app_infoService.getList(pid);
		return list;
	}


	//跳转appadd
	@RequestMapping(value="/jumpappadd.html")
	public String jumpappadd(){	
		return "developer/appadd";
	}


	//App所属平台
	@RequestMapping(value="/platform.json",method=RequestMethod.GET)
	@ResponseBody
	public List<data_dictionary> platform()throws Exception{
		List<data_dictionary> list=new ArrayList<data_dictionary>();
		list=app_infoService.type();
		return list;
	}


	//AKPName重名验证
	@RequestMapping(value="/apkname.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkname(@RequestParam("APKName")String APKName)throws Exception{
		HashMap<String,String> resultMap=new HashMap<String,String>();

		if(APKName==""||APKName==null){
			resultMap.put("APKName","empty");
		}else{
			if(app_infoService.APKName(APKName)>0){
				resultMap.put("APKName","exist");
			}else{
				resultMap.put("APKName","noexist");
			}	
		}
		return resultMap;
	}


	//新增App信息
	@RequestMapping(value="/AppAdd.html",method=RequestMethod.POST)
	public String AppAdd(app_info app,
			Model model,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value="attach",required=false)MultipartFile attach)throws Exception{

		String a_logoPicPath=null;

		String path=request.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		String oldFileName=attach.getOriginalFilename();//原文件名
		String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
		int filesize=5000000;

		if(attach.getSize()>filesize){
			request.setAttribute("uploadFileError", "上传文件不能大于5000Kb");
			return "developer/appadd";
		}else if(prefix.equalsIgnoreCase("jpg")
				||prefix.equalsIgnoreCase("png")
				||prefix.equalsIgnoreCase("jpeg")
				||prefix.equalsIgnoreCase("gif")){
			String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_ProviderNal.jpg";
			File targetFile=new File(path,fileName);
			if(!targetFile.exists()){
				targetFile.mkdirs();
			}

			//保存
			try {
				attach.transferTo(targetFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				request.setAttribute("uploadFileError","上传失败!");
				return "developer/appadd";
			}
			a_logoPicPath=path+File.separator+fileName;	

		}else{
			request.setAttribute("uploadFileError","上传图片格式不正确");
			return "developer/appadd";
		}

		dev_user user=(dev_user)session.getAttribute("devuser");
		int devId=user.getId();

		app.setCreationDate(new Date());
		String[] chai=a_logoPicPath.split("\\\\");
		String wangzong="";
		for (int i = 0; i < chai.length; i++) {
			if(i>=3){
				wangzong+="/"+chai[i];
			}
		}
		app.setLogoPicPath(wangzong);
		app.setLogoLocPath(a_logoPicPath);
		app.setDevId(devId);

		if(app_infoService.addApp(app)){
			return "redirect:/developer/gettype.html";
		}else{
			request.setAttribute("uploadFileError","error");
			return "developer/appadd";
		}


	}

	/**
    //APP上下架
    @RequestMapping(value="/{appid}/sale",method=RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String appid,HttpSession session){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Integer appIdInteger = 0;
		try{
			appIdInteger = Integer.parseInt(appid);
		}catch(Exception e){
			appIdInteger = 0;
		}
		resultMap.put("errorCode", "0");
		resultMap.put("appId", appid);
		if(appIdInteger>0){
			try {
				dev_user devUser = (dev_user)session.getAttribute("user");
				app_info appInfo = new app_info();
				appInfo.setId(appIdInteger);
				appInfo.setModifyBy(devUser.getId());
				if(app_infoService.){
					resultMap.put("resultMsg", "success");
				}else{
					resultMap.put("resultMsg", "success");
				}		
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		}else{
			//errorCode:0为正常
			resultMap.put("errorCode", "param000001");
		}


		return resultMap;
	}
	 */

	//新增版本页面
	@RequestMapping(value="/appversionadd")
	public String appversionadd(@RequestParam("id") String id,
			HttpSession session)throws Exception{
		List<app_version> list=new ArrayList<app_version>();
		int newid=Integer.parseInt(id);
		list=app_versionService.getVersionList(newid);
		session.setAttribute("appId",newid);
		session.setAttribute("appVersionList",list);
		return "developer/appversion";
	}

	//新增版本
	@RequestMapping(value="/addversion.html",method=RequestMethod.POST)
	public String addversion(app_version ve,
			HttpServletRequest request,
			@RequestParam("appId")String appId,
			@RequestParam(value="attach",required=false)MultipartFile attach)throws Exception{

		String a_downloadLink=null;

		String path=request.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		String oldFileName=attach.getOriginalFilename();//原文件名
		String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
		int filesize=5000000;

		if(attach.getSize()>filesize){
			request.setAttribute("fileUploadError", "上传文件不能大于5000Kb");
			return "developer/appversion";
		}else if(prefix.equalsIgnoreCase("apk")){
			String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_ProviderNal.apk";
			File targetFile=new File(path,fileName);
			if(!targetFile.exists()){
				targetFile.mkdirs();
			}

			//保存
			try {
				attach.transferTo(targetFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				request.setAttribute("fileUploadError","上传失败!");
				return "developer/appversion";
			}
			a_downloadLink=path+File.separator+fileName;	

		}else{
			request.setAttribute("fileUploadError","上传apk文件格式不正确");
			return "developer/appversion";
		}



		int id=Integer.parseInt(appId);
		ve.setAppId(id);
		ve.setCreationDate(new Date());
		String[] chai=a_downloadLink.split("\\\\");
		String wangzong="";
		for (int i = 0; i < chai.length; i++) {
			if(i>=3){
				wangzong+="/"+chai[i];
			}
		}
		ve.setApkLocPath(a_downloadLink);
		ve.setDownloadLink(wangzong);
		ve.setApkFileName(oldFileName);
		if(app_versionService.addVersion(ve)){
			app_info app=new app_info();
			app.setId(id);
			app.setVersionId(ve.getId());
			app_infoService.updateApp(app);
			return "redirect:/developer/gettype.html";
		}else{
			return "developer/appversion"; 
		}

	}

	//修改版本页面
	@RequestMapping(value="/appversionupdate.html")
	public String appversionupdate(@RequestParam("aid") String aid,
			@RequestParam("vid") String vid,
			HttpSession session)throws Exception{
		List<app_version> list=new ArrayList<app_version>();
		int newaid=Integer.parseInt(aid);
		int newvid=Integer.parseInt(vid);
		list=app_versionService.getVersionList(newaid);
		app_version ve=new app_version();
		ve=app_versionService.getOne(newvid);
		session.setAttribute("appVersion", ve);
		session.setAttribute("appVersionList",list);
		return "developer/updateversion";
	}

	//删除apk文件
	@RequestMapping(value="/delapk.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delapk(@RequestParam("id")String id)throws Exception{
		HashMap<String,String> resultMap=new HashMap<String,String>();
		int newid=Integer.parseInt(id);
		app_version ve=app_versionService.getOne(newid);
		if(app_versionService.deleteAPK(ve)){
			File file=new File(ve.getApkLocPath());
			file.delete();
			resultMap.put("result","success");
		}else{
			resultMap.put("result","failed");
		}	
		return resultMap;
	}
	
	//修改版本
	@RequestMapping(value="updateversion.html",method=RequestMethod.POST)
	public String updateversion(app_version ve,
			HttpServletRequest request,
			@RequestParam("vid")String vid,
			@RequestParam(value="attach",required=false)MultipartFile attach)throws Exception{


		if(!attach.isEmpty()){
			String a_downloadLink=null;

			String path=request.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName=attach.getOriginalFilename();//原文件名
			String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize=5000000;

			if(attach.getSize()>filesize){
				request.setAttribute("fileUploadError", "上传文件不能大于5000Kb");
				return "developer/updateversion";
			}else if(prefix.equalsIgnoreCase("apk")){
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_ProviderNal.apk";
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}

				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError","上传失败!");
					return "developer/updateversion";
				}
				a_downloadLink=path+File.separator+fileName;	

			}else{
				request.setAttribute("fileUploadError","上传图片格式不正确");
				return "developer/updateversion";
			}


			String[] chai=a_downloadLink.split("\\\\");
			String wangzong="";
			for (int i = 0; i < chai.length; i++) {
				if(i>=3){
					wangzong+="/"+chai[i];
				}
			}
			ve.setApkLocPath(a_downloadLink);
			ve.setDownloadLink(wangzong);
			ve.setApkFileName(oldFileName);
		}

		int id=Integer.parseInt(vid);
		ve.setId(id);
		ve.setModifyDate(new Date());

		if(app_versionService.updateversion(ve)){
			return "redirect:/developer/gettype.html";
		}else{
			return "developer/updateversion";
		}
	}

	@RequestMapping(value="appinfomodify")
	public String appinfomodify(@RequestParam("id") String id,
			HttpSession session)throws Exception{
		int newid=Integer.parseInt(id);
		app_info app=new app_info();
		app=app_infoService.getOne(newid);
		session.setAttribute("appInfo", app);
		return "developer/updateappinfo";
	}

	//删除图片
	@RequestMapping(value="/delfile.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delfile(@RequestParam("id")String id)throws Exception{
		HashMap<String,String> resultMap=new HashMap<String,String>();
		int newid=Integer.parseInt(id);
		app_info app=app_infoService.getOne(newid);
		if(app_infoService.deleteLogo(newid)){
			File file=new File(app.getLogoLocPath());
			file.delete();
			resultMap.put("result","success");
		}else{
			resultMap.put("result","failed");
		}	
		return resultMap;
	}


	//修改APP信息
	@RequestMapping(value="appinfoupdate.html",method=RequestMethod.POST)
	public String appinfoupdate(app_info app,
			HttpServletRequest request,
			@RequestParam(value="attach",required=false)MultipartFile attach)throws Exception{



		if(!attach.isEmpty()){        //判断MultipartFile是否有元素，有元素就进入上传
			String a_downloadLink=null;

			String path=request.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName=attach.getOriginalFilename();//原文件名
			String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize=5000000;

			if(attach.getSize()>filesize){
				request.setAttribute("fileUploadError", "上传文件不能大于5000Kb");
				return "developer/updateappinfo";
			}else if(prefix.equalsIgnoreCase("jpg")
					||prefix.equalsIgnoreCase("png")
					||prefix.equalsIgnoreCase("jpeg")
					||prefix.equalsIgnoreCase("gif")){
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_ProviderNal.jpg";
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}

				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.setAttribute("fileUploadError","上传失败!");
					return "developer/updateappinfo";
				}
				a_downloadLink=path+File.separator+fileName;	

			}else{
				request.setAttribute("fileUploadError","上传图片格式不正确");
				return "developer/updateappinfo";
			}


			String[] chai=a_downloadLink.split("\\\\");
			String wangzong="";
			for (int i = 0; i < chai.length; i++) {
				if(i>=3){
					wangzong+="/"+chai[i];
				}
			}
			app.setLogoPicPath(wangzong);
			app.setLogoLocPath(a_downloadLink);
		}
		app.setUpdateDate(new Date());
		if(app_infoService.updateApp(app)){
			return "redirect:/developer/gettype.html";
		}else{
			return "developer/updateappinfo";
		}
	}

	//APP详情跳转页面
	@RequestMapping(value="appview")
	public String appview(@RequestParam("id")String id,
			HttpSession session)throws Exception{
		int newid=Integer.parseInt(id);
		app_info app=new app_info();
		app=app_infoService.getOne(newid);
		session.setAttribute("appInfo",app);
		List<app_version> list=new ArrayList<app_version>();
		list=app_versionService.getVersionList(newid);
		session.setAttribute("appVersionList",list);
		return "developer/appinfoview";
	}

	//删除App
	@RequestMapping(value="/delapp.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delapp(@RequestParam("id")String id)throws Exception{
		HashMap<String,String> resultMap=new HashMap<String,String>();
		int newid=Integer.parseInt(id);
		if(app_infoService.deleteApp(newid)){
			resultMap.put("delResult","true");
		}else{
			resultMap.put("delResult","false");
		}	
		return resultMap;
	}

	//上下架
	@RequestMapping(value="sale",method=RequestMethod.GET)
	@ResponseBody
	public Object updown(@RequestParam("appId") String appid,HttpSession session)throws Exception{
		HashMap<String,String> resultMap=new HashMap<String,String>();
		int newid=Integer.parseInt(appid);
		app_info app=app_infoService.getOne(newid);
		if(app_infoService.updateStatus(app)){
			resultMap.put("resultMsg","success");
		}else{
			resultMap.put("resultMsg","failed");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/logout.html")
	public String logout(HttpSession session){
		if(session.getAttribute("devuser")!=null){
			session.invalidate();
		}
		return "main";
	}


}
