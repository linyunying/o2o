package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.Area;

/**
 * mybatis的接口式编程
 * 定义一个区域接口
 * 与AreaDao配置文件相对应
 * @author Administrator
 *
 */
/*
 * 利用Mybatis框架该接口无需实现类，直接就可以利用接口调用抽象方法
 */
public interface AreaDao {

	/**
	 * 列出区域列表
	 * @return areaList
	 */
	//对应的是AreaDao.xml配置文件
	//返回一个Area对象集合
	List<Area> queryArea();
	
}
