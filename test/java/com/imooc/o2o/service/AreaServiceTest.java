
package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;


public class AreaServiceTest  extends BaseTest{

	//自动注入bean对象
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CacheService cacheService;
	
	//测试方法
	@SuppressWarnings("static-access")
	@Test
	public void  testGetAreaList()
	{
		List<Area> areaList=areaService.getAreaList();
		
		assertEquals("西苑",areaList.get(0).getAreaName());
		cacheService.removeFromCache(areaService.AREALISTKEY);
		areaList=areaService.getAreaList();
		System.out.println(areaList.get(0).getAreaName());
	}
}
