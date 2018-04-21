package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.ShopCategory;

public interface ShopCategoryService {

	//查询店铺类别列表
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondtion);
}
