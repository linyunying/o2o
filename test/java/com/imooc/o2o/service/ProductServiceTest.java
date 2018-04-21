package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;


public class ProductServiceTest extends BaseTest {
	@Autowired
	private ProductService productService;

	/**
	 * 添加商品信息
	 * @throws ShopOperationException
	 * @throws FileNotFoundException
	 */
	@Test
	@Ignore
	public void testAddProduct() throws ShopOperationException, FileNotFoundException {
		// 创建shopId为1且productCategoryId为1的商品实例并给其成员变量赋值
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("测试商品1");
		product.setProductDesc("测试商品1");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		// 创建缩略图文件流
		File thumbnailFile = new File("E:/image/baoman2.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImgHolder thumbnail = new ImgHolder(is,thumbnailFile.getName());
		// 创建两个商品详情图文件流并将他们添加到详情图列表中
		File productImg1 = new File("E:/image/QQ截图3.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("E:/image/QQ截图4.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImgHolder> productImgList = new ArrayList<ImgHolder>();
		productImgList.add(new ImgHolder(is1,productImg1.getName()));
		productImgList.add(new ImgHolder( is2,productImg2.getName()));
		// 添加商品并验证
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}


	/**
	 * 修改product信息以及详情图片信息
	 * @throws ShopOperationException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testModifyProduct() throws ShopOperationException, FileNotFoundException {
		// 创建shopId为1且productCategoryId为1的商品实例并给其成员变量赋值
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("正式的商品");
		product.setProductDesc("正式的商品");
		// 创建缩略图文件流
		File thumbnailFile = new File("E:/image/ercode.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImgHolder thumbnail = new ImgHolder(is,thumbnailFile.getName());
		// 创建两个商品详情图文件流并将他们添加到详情图列表中
		File productImg1 = new File("E:/image/QQ截图3.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("E:/image/QQ截图4.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImgHolder> productImgList = new ArrayList<ImgHolder>();
		productImgList.add(new ImgHolder(is1,productImg1.getName()));
		productImgList.add(new ImgHolder(is2,productImg2.getName()));
		// 添加商品并验证
		ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}

	/**
	 * 查询商品信息
	 */
	
}
