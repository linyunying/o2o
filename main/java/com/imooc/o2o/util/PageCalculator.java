package com.imooc.o2o.util;


/**
 * 将前端传来的页数转换为后端分页查询所需要的条数
 * @author Administrator
 *
 */
public class PageCalculator {
	/**
	 * 将页数转为条数
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static int calculateRowIndex(int pageIndex,int pageSize){
		/*
		 * 如果页数大于0，则采用公式：（页数-1）*当前页显示行数
		 * 如果小于或等于0，则条数直接为0
		 */
		return (pageIndex>0)?(pageIndex-1)*pageSize:0;
	}
}
