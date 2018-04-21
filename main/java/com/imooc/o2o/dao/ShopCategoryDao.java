package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.imooc.o2o.entity.ShopCategory;

public interface ShopCategoryDao {

	/*
	 * 商铺列表，传入参数ShopCategory
	 * 之所以ShopCategory对象是因为可能根据商铺类别的名称，时间等多个属性查找ShopCategory类别
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondtion")
	ShopCategory shopCategoryCondtion);
}
