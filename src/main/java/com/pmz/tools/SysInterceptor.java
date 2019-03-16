package com.pmz.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pmz.pojo.backend_user;
import com.pmz.pojo.dev_user;

public class SysInterceptor extends HandlerInterceptorAdapter{
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object handler)throws Exception{
		HttpSession session=request.getSession();
		backend_user bu=(backend_user)session.getAttribute("backuser");
		dev_user du=(dev_user)session.getAttribute("devuser");
		if(bu!=null){
			return true;
		}else if(du!=null){
			return true;
		}else{
			response.sendRedirect(request.getContextPath()+"/jsp/404.jsp");
			return false;
		}
	}

}
