package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 接收用户输入的验证码并判断验证码是否有误
 * @author Administrator
 *
 */
public class CodeUtil {

	public static boolean checkVerifyCode(HttpServletRequest request){
		/*
		 * 获取kaptcha的正确验证码
		 */
		String verifyCodeExpected=(String)request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		
		
		/*
		 * 从前端formdata对象当中获取用户输入的验证码
		 * 将获取到的信息转换为字符串
		 * 获取用户输入的验证码
		 */
		String verifyCodeActual=HttpServletRequestUtil.getString(request, "verifyCodeActual");
		/*
		 * 如果用户输入的验证码为空或者输入的验证码与实际验证码不符合，则
		 */	
		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected)){
			return false;
		}
			return true;
		
	}
}
