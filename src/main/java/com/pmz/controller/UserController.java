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
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.pmz.pojo.backend_user;
import com.pmz.pojo.dev_user;
import com.pmz.service.backend_user.backend_userService;
import com.pmz.service.dev_user.dev_userService;
import com.pmz.tools.BaseConvert;
import com.pmz.tools.Constants;
import com.sun.org.apache.regexp.internal.recompile;

import sun.misc.Hashing;

@Controller
public class UserController extends BaseConvert{
	private Logger logger=Logger.getLogger(UserController.class);

	@Resource dev_userService dev_userService;
	@Resource backend_userService backend_userService;
	
	
	@RequestMapping(value="/gologin.html")
	public String goLogin(@RequestParam String checked,HttpSession session){
		int id=Integer.parseInt(checked);
		session.setAttribute("checkid",id);
		return "login";
	}
	
	
	
	@RequestMapping(value="/login.html",method=RequestMethod.POST)
	public String doLogin(@RequestParam String devCode,
			              @RequestParam String devPassword,
			              HttpSession session,
			              HttpServletRequest requset)throws Exception{
		logger.debug("doLogin>>>>>>>>>>>>>>>>>>>>>>>");
		
		int id=(Integer)session.getAttribute("checkid");
		if(id==1){
			backend_user user=backend_userService.Login(devCode,devPassword);
			if(null!=user){		
				session.setAttribute("backuser",user);
			    return "backend/welcome";	
			}else{
				requset.setAttribute("error","错误");
				return "login";
				
			}
		}else{
			dev_user user=dev_userService.Login(devCode, devPassword);
			if(null!=user){		
				session.setAttribute("devuser",user);
			    return "developer/welcome";	
			}else{
				requset.setAttribute("error","错误");
				return "login";
				
			}
		}		
	}
	
	

	
	

	
}
