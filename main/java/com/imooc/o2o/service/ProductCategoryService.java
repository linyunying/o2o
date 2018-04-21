package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;


/**
 * 查询指定某个店铺下的所有商品类别
 * @author Administrator
 *
 */
public interface ProductCategoryService {

	/**
	 * 根据商铺id查询商品类别列表
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 批量增加商品类别，因为操作数据库并改变数据库，所以必须抛出支持事物回滚的异常
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution  bathAddtProductCategory(List<ProductCategory> productCategoryList)throws ProductCategoryOperationException;
	

	/**
	 * 将此类别下的商品里的类别id置为空，再删除商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)throws ProductCategoryOperationException;
	
}
