package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;

/**
 * 测试AreaDao层方法
 * @author Administrator
 *
 */
//继承BaseTest整合类
public class AreaDaoTest extends BaseTest {

	//使用注解自动为变量赋值.，相当于自动将bean对象赋值到该变量下
	@Autowired
	private AreaDao areaDao;
	@Test
	public void testQueryArea()
	{
		//使用mybatis框架，接口无需实现类即可直接调用抽象方法，因为mybatis框架会自动为该接口实现类
		//AreaDao对象的中的queryArea方法，并且返回的是一个集合
		List<Area> areaList=areaDao.queryArea();
		
		/*
		 * 测试集合大小是否和预期值一致
		 * 使用Junit测试方法，前面2是我们的预期值，后面是结果值
		 */
		
		//如果预期值与结果值一致，则测试通过
		assertEquals(2,areaList.size());
		System.out.println(areaList.get(0).getAreaName());
	}
}
