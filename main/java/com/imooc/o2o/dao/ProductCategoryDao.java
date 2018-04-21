package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;

/**
 * 商品类别
 * @author Administrator
 *
 */
public interface ProductCategoryDao {

	
	
	/**
	 * 根据ShopId查找出该店铺的所有商品类别
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);

	/**
	 * 批量添加商品类别信息
	 * @param productCategoryList
	 * @return
	 */
	int bathInsertProductCategoryList(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;


	/**
	 * 删除指定商品类别
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategory,@Param("shopId") long shopId)throws ProductCategoryOperationException;
}
