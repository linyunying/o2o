package com.imooc.o2o.dao;


import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

/**
 * ShopDao测试类，需要继承测试套件，
 * 加载spring-dao配置文件,否则会报空指针异常
 * @author Administrator
 *
 */
public class ShopDaoTest extends BaseTest {

	@Autowired
	private ShopDao shopDao;
	
	
	@Test
	public void testQueryShopListAndCount(){
		Shop shopCondition=new Shop();
		ShopCategory child=new ShopCategory();
		ShopCategory parent=new ShopCategory();
		parent.setShopCategoryId(12l);
		child.setParent(parent);
		shopCondition.setShopCategory(child);
	 
		
		List<Shop> shopList=shopDao.queryShopList(shopCondition, 0,6);
		int count=shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表的大小："+shopList.size());
		System.out.println("总条数"+count);
	
	}
	
	
	/**
	 * 查询店铺测试方法
	 */
	@Test
	@Ignore
	public void testQueryByShopId(){
		long shopId=1;
		
		Shop shop=shopDao.queryByShopId(shopId);
		
		System.out.println(shop.getArea().getAreaName());
		
	}
	
	/**
	 * 注册店铺测试方法
	 */
	@Test
	//加上以下注解则测试运行时不会执行该方法
	@Ignore
	public void testInsertShop()
	{
		Shop shop=new Shop();
		
		PersonInfo owner=new PersonInfo();
		
		ShopCategory shopCategory=new ShopCategory();
		
		Area area=new Area();
		
		owner.setUserId(1l);
		
		shopCategory.setShopCategoryId(1l);
		
		area.setAreaId(2);
		
		shop.setArea(area);
	
		shop.setOwner(owner);
		
		shop.setShopCategory(shopCategory);
		
		shop.setShopName("测试的店铺");
		
		shop.setShopDesc("test");
		
		shop.setShopAddr("test");
		
		shop.setPhone("test");
		
		shop.setShopImg("test");
		
		shop.setCreateTime(new Date());
		
		shop.setEnableStatus(1);
		
		shop.setAdvice("审核中");
		
		//调用接口的抽象方法执行sql语句
		//抽象方法返回的是执行sql语句时，受影响的行数
		int effectedNum=shopDao.insertShop(shop);
		
		assertEquals(1,effectedNum);
	}
	
	/**
	 * 更新店铺功能测试方法
	 */
	@Test
	@Ignore
	public void testUpdateShop()
	{
		/**
		 * 将shop商铺信息进行修改
		 */
		Shop shop=new Shop();

		shop.setShopId(1l);
		
		shop.setShopDesc("测试描述");
		
		shop.setShopAddr("测试地址");

		shop.setLastEditTime(new Date());
		 
		
		//调用接口的抽象方法执行sql语句
		//抽象方法返回的是执行sql语句时，受影响的行数
		int effectedNum=shopDao.updateShop(shop);
		
		assertEquals(1,effectedNum);
	}
	
}
