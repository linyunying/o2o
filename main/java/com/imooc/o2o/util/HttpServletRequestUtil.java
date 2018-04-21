package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理前端商铺信息保存在HttpServletRequest参数转化问题
 * 商铺注册时，每个填写的信息都保存在HttpServletRequest参数中
 * @author Administrator
 *
 */
public class HttpServletRequestUtil {



	/**
	 * 定义转换方法的原因：
	 * 因为request获取到的参数返回类型是String,而我们实体类中的参数含各种类型
	 * String ss=request.getParameter("参数");
	 * 所以我们需要采用解码方式，将String解码为各种形式，如int，Long，Double，boolean
	 * 之所以不能采用强转方式是因为有的类型(不能将String类型强转为int，Long，Double，boolean类型)是不支持直接强转的，
	 * 所以需要采用解码方式转换成我们需要的类型
	 * 我们定义以下转换方法，当我们需要的时候直接调用该转换方法即可
	 * 
	 */
	public static int getInt(HttpServletRequest request,String key){
		try{
			/*
			 * 将前端传来的参数采用decode方式解码
			 */
			return Integer.decode(request.getParameter(key));
		
			//如果转换失败则返回-1
		}catch(Exception e){
			return -1;
		}
		
		
	}
	
	
	public static Long getLong(HttpServletRequest request,String key){
		try{
		
			return Long.valueOf(request.getParameter(key));
		
			//如果转换失败则返回-1
		}catch(Exception e){
			return -1l;
		}
	}
	
	
	public static Double getDouble(HttpServletRequest request,String key){
		try{
			//商铺信息有部分可能为shop实体类中的Long类型属性，所以通过获取参数名获得参数值，将参数值转为int类型属性值
			return Double.valueOf(request.getParameter(key));
		
			//如果转换失败则返回-1
		}catch(Exception e){
			return -1d;
		}
	}
	

	public static boolean getboolean(HttpServletRequest request,String key){
		try{
			//商铺信息有部分可能为shop实体类中的Long类型属性，所以通过获取参数名获得参数值，将参数值转为int类型属性值
			return Boolean.valueOf(request.getParameter(key));
		
			//如果转换失败则返回-1
		}catch(Exception e){
			return false;
		}
	}
	
	public static String getString(HttpServletRequest request,String key){
		try{
			String result=request.getParameter(key);
			if(result!=null){
				//将前后空白去掉
				result=result.trim();
			}
			/*
			 * 空字符串也算为字符串，
			 * 所以我们如果获取到的是空字符串则直接将结果返回空值
			 */
			if("".equals(result)){
				result=null;
			}
			return result;
			/*
			 * 如果没有获取到字符串则返回空值
			 */
		}catch(Exception e){
			return null;
		}
	}
}
