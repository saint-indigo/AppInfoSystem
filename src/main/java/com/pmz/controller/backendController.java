package com.pmz.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmz.pojo.app_category;
import com.pmz.pojo.app_info;
import com.pmz.pojo.app_version;
import com.pmz.pojo.data_dictionary;
import com.pmz.pojo.dev_user;
import com.pmz.service.app_info.app_infoService;
import com.pmz.service.app_info.app_infoServiceImpl;
import com.pmz.service.app_version.app_versionService;
import com.pmz.tools.BaseConvert;
import com.pmz.tools.Constants;
import com.pmz.tools.PageSupport;

@Controller
@RequestMapping("/backend")
public class backendController extends BaseConvert{


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



		app.setDevId(0);

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

		return "backend/applist";
	}



	//App123分类
	@RequestMapping(value="/getlist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<app_category> gettt(@RequestParam("pid")Integer pid, Model model)throws Exception{
		List<app_category> list=new ArrayList<app_category>();
		list=app_infoService.getList(pid);
		return list;
	}


	//审核页面
	@RequestMapping(value="/check")
	public String check(@RequestParam("aid")Integer aid,
			@RequestParam("vid")Integer vid,
			HttpSession session)throws Exception{
		app_info app=app_infoService.getOne(aid);
		app_version ev=app_versionService.getOne(vid);
		session.setAttribute("appInfo",app);
		session.setAttribute("appVersion",ev);
		return "backend/appcheck";
	}

	//审核提交
	@RequestMapping(value="/checksave")
	public String checksave(@RequestParam("appid")Integer appid,
			@RequestParam("status")Integer status)throws Exception{
		app_info app=new app_info();
		app.setId(appid);
		app.setStatus(status);
		if(app_infoService.checkStatus(app)){
			return "redirect:/backend/gettype.html";
		}else{
			return "backend/appcheck";
		}
	}
	
	//登出
	@RequestMapping(value="/logout.html")
	public String logout(HttpSession session){
		if(session.getAttribute("backuser")!=null){
			session.invalidate();
		}
		return "main";
	}
	
	//表格
	@RequestMapping(value="/excel.html")
	public void excel(HttpServletResponse response)throws Exception{
		response.setHeader("Content-disposition", "attachment; filename=applist.xls");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
		WritableWorkbook book=Workbook.createWorkbook(response.getOutputStream());
		WritableSheet sheet=book.createSheet("APPINFO表",0);
		// 表字段名
        sheet.addCell(new jxl.write.Label(0, 0, "软件名称"));
        sheet.addCell(new jxl.write.Label(1, 0, "APK名称"));
        sheet.addCell(new jxl.write.Label(2, 0, "软件大小(单位:M)"));
        sheet.addCell(new jxl.write.Label(3, 0, "所属平台"));
        sheet.addCell(new jxl.write.Label(4, 0, "所属分类(一级分类、二级分类、三级分类)"));
        sheet.addCell(new jxl.write.Label(5, 0, "状态"));
        sheet.addCell(new jxl.write.Label(6, 0, "下载次数"));
        sheet.addCell(new jxl.write.Label(7, 0, "最新版本号"));

        
        List<app_info> list=app_infoService.getAllList(null, null, null, null, null, null, null, 1,9999);
        
        for (int i = 0; i <list.size(); i++) {
        	app_info app=list.get(i);
        	sheet.addCell(new jxl.write.Label(0,i+1,app.getSoftwareName()));
        	sheet.addCell(new jxl.write.Label(1,i+1,app.getAPKName()));
        	sheet.addCell(new jxl.write.Label(2,i+1,""+app.getSoftwareSize()));
        	sheet.addCell(new jxl.write.Label(3,i+1,app.getPingtai()));
        	sheet.addCell(new jxl.write.Label(4,i+1,app.getLei1()+">"+app.getLei2()+">"+app.getLei3()));
        	sheet.addCell(new jxl.write.Label(5,i+1,app.getZhuangtai()));
        	sheet.addCell(new jxl.write.Label(6,i+1,""+app.getDownloads()));
        	sheet.addCell(new jxl.write.Label(7,i+1,app.getVersionNo()));
		}
        
        book.write();
        book.close();
        
	}

}
