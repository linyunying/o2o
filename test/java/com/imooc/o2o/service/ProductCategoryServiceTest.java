package com.imooc.o2o.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;

public class ProductCategoryServiceTest extends BaseTest {

	@Autowired
	ProductCategoryService productCategoryService;
	
	@Test
	public void productCategoryServiceTest(){
		
		List<ProductCategory> pList=productCategoryService.getProductCategoryList(29l);
		
		//如果预期值与结果值一致，则测试通过
				//assertEquals(9,pList.size());
		System.out.println("service层的商品总数："+pList.size());
		
	}
	
	
}
