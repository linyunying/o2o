package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 访问路由
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/local")
public class LocalController {

	/**
	 * 绑定帐号页面路由
	 */
	@RequestMapping(value="/accountbind",method=RequestMethod.GET)
	private String accountbind(){
		return "local/accountbind";
	}
	
	/**
	 * 修改密码路由
	 */
	@RequestMapping(value="/changepsw",method=RequestMethod.GET)
	private String changepsw(){
		return "local/changepsw";
	}
	
	/**
	 * 登录页面路由
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	private String login(){
		return "local/login";
	}
}

