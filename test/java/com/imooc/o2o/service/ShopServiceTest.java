package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;


public class ShopServiceTest extends BaseTest{

	@Autowired
	private ShopService shopService;

	
	@Test
	public void testGetShopList(){
		Shop shopCondition=new Shop();
		
		ShopCategory sCategory=new ShopCategory();
		sCategory.setShopCategoryId(2l);
		shopCondition.setShopCategory(sCategory);

		ShopExecution sExecution=shopService.getQueryShopList(shopCondition, 1, 2);

	System.out.println("店铺列表数"+sExecution.getShopList().size());
	System.out.println("店铺总数"+sExecution.getCount());
	
		
	
	}
	
	@Test
	@Ignore
	public void testModifyShop()throws ShopOperationException,FileNotFoundException{
		Shop shop=new Shop();
		shop.setShopId(1l);
		shop.setShopName("更改后的店铺名称");

		File shopImg=new File("E:/image/dabai.jpg");
		InputStream is=new FileInputStream(shopImg);
		
		ImgHolder imgHolder=new ImgHolder(is, "dabai.jpg");
		
		ShopExecution shopExecution=shopService.modifyShop(shop, imgHolder);
		System.out.println("图片地址："+shopExecution.getShop().getShopImg());
	}
	
	
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException{

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
		
		shop.setShopName("测试的店铺3");
		
		shop.setShopDesc("test3");
		
		shop.setShopAddr("test3");
		
		shop.setPhone("test3");	
		
		
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		
		shop.setAdvice("审核中");
		
		File shopImg=new File("E:/image/xiaohuangren.jpg");
		InputStream is=new FileInputStream(shopImg);
		
		ImgHolder imgHolder=new ImgHolder(is, shopImg.getName());
		ShopExecution se=shopService.addShop(shop, imgHolder);
		
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
}

