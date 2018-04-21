package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.Area;

/**
 * AreaService接口
 * @author Administrator
 *
 */
public interface AreaService {

	public  static final String AREALISTKEY="arealist";
	
	//定义抽象方法，返回AreaList集合
	List<Area> getAreaList();
	
}
