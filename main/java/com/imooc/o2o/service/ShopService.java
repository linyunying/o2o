package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

/**
 * 店铺操作
 * @author Administrator
 *
 */
public interface ShopService {

	/**
	 * 进行分页查询店铺信息
	 * 
	 * @param shopCondition
	 * @param pageIndex前端返回的当前页码数，数据库当中是以条数开始，如从0条开始，然后需要查询几条
	 * 所以我们需要公式将前端当前页码数转为后端SQL语句分页查询所需要的条数
	 * @param pageSize当前页共几行
	 * @return
	 */
	public ShopExecution getQueryShopList(Shop shopCondition,int pageIndex,int pageSize);
	
	/**
	 * 根据shopId获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getQueryByShopId(long shopId) throws ShopOperationException;
	
	/**
	 * 涉及到事物的处理，所以我们必须抛出支持事物回滚的错误
	 * 
	 * 修改店铺信息以及店铺图片
	 * @param shop
	 * @param shopImgInputStream
	 * @return
	 */
	ShopExecution modifyShop(Shop shop,ImgHolder thumbnail) throws ShopOperationException;
	
	/**
	 * 添加店铺信息和店铺图片路径
	 * @param shop
	 * @param shopImg
	 * @return返回添加店铺的执行状态，判断用户店铺是否添加成功
	 */
	/*
	 * 之所以传入文件名是因为不能通过inputStream获取文件名
	 */
	ShopExecution addShop(Shop shop ,ImgHolder thumbnail) throws ShopOperationException;
}
