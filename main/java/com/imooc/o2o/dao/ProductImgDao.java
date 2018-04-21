package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductImg;

/**
 * 商品详情图片
 * @author Administrator
 *
 */
public interface ProductImgDao {

	/**
	 * 查询商品详情图
	 * @param product
	 * @return
	 */
	
	List<ProductImg> queryProductImgList(long product);
	
	/**
	 * 批量增加商品详情图
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);

	/**
	 * 删除指定商品下的所有详情图
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
}
