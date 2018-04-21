package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;


/**
 * 设置Junit回环，即让test按照指定方法执行
 * @author Administrator
 *
 */
//按照名称执行测试方法

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	ProductCategoryDao productCategoryDao;
	
	
	/**
	 * 查询商品类别信息
	 */
	
	@Test
	public void testBQueryProductCategoryDaoTest(){
		
		
		List<ProductCategory> pr=productCategoryDao.queryProductCategoryList(1l);
		
		//如果预期值与结果值一致，则测试通过
	//	assertEquals(2,pr.size());
		
		System.out.println("商品总数:"+pr.size());
		
	}
	
	
	/**
	 * 插入商品类别信息
	 */
	@Test
	@Ignore
	public void testABathInsertProductCategor(){
		ProductCategory productCategory=new ProductCategory();
		
		productCategory.setProductCategoryName("商品类别10");
		
		productCategory.setCreateTime(new Date());
		
		productCategory.setShopId(1l);
		
		productCategory.setPriority(30);
		
		
		ProductCategory productCategory2=new ProductCategory();
				
		productCategory2.setProductCategoryName("商品类别11");
				
		productCategory2.setCreateTime(new Date());
				
		productCategory2.setShopId(1l);
				
		productCategory2.setPriority(31);
		
		List<ProductCategory> productCategoryList=new ArrayList<ProductCategory>();
		
		productCategoryList.add(productCategory);
		
		productCategoryList.add(productCategory2);
		
		int effer=productCategoryDao.bathInsertProductCategoryList(productCategoryList);
	
		assertEquals(2, effer);
		
		System.out.println("添加商品");
		
	}
	
	@Test
	@Ignore
	public void testCDeleteProductCategory()throws Exception{
		
		long shopId=1;
		List<ProductCategory> productCategories=productCategoryDao.queryProductCategoryList(shopId);
		
		for(ProductCategory pc:productCategories){
			if ("商品类别10".equals(pc.getProductCategoryName())||"商品类别11".equals(pc.getProductCategoryName())) {
				int effectedNum=productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1, effectedNum);
				
			}
		}
		
		System.out.println("删除商品类别");
	}
}
