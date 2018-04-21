package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {

	/**
	 * 查询商品列表并分页，可输入的条件有：
	 * 商品名（支持模糊查询），商品状态，店铺Id,商品类别
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition,int rowIndex,int pageSize);
	
	/**
	 * 添加商品信息，商品缩略图，商品详情图
	 * 将图片和图片名称保存在同一个类当中
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImgHolder thumbnail,List<ImgHolder> productImgList)throws ProductOperationException;


	/**
	 * 通过productId查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	
	/**
	 * 修改商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	
	ProductExecution modifyProduct(Product product,ImgHolder thumbnail,List<ImgHolder> productImgHolderList)throws ProductOperationException;
}
