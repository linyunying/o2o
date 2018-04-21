package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

/**
 * 注册及更新店铺信息
 * @author Administrator
 *
 */

public interface ShopDao {

	/**
	 *查询店家的所有店铺，若一个店家有过多店铺则支持分页查询
	 *分页查询店铺，可输入的条件有：
	 *店铺名(支持模糊查询),店铺状态，
	 *店铺类别，区域ID，用户owner
	 * 
	 * @param shopCondition
	 * @param rowIndex：从第几行开始取数据
	 * (就是比如limit 0,5 则是从第0条开始，当前页显示出五条商铺信息。
	 * limit 5,6则第二页是从第五条开始显示，当前页显示六条商铺信息)
	 * @param pageSize：返回的条数，就是当前页可以显示出几条商铺信息
	 * @return
	 */
	//加上param注解进行唯一标识
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	

	/**
	 * 返回查询出来的店铺总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	/**
	 * 根据店铺ID查询店铺信息并返回
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);
	
	/**
	 * 注册店铺
	 * 将商铺信息添加到数据库tb_shop表当中
	 * @param shop
	 * @return 执行sql语句时，数据库影响行数。返回-1则代表插入信息失败
	 */
	int insertShop(Shop shop) throws ShopOperationException;
	
	/**
	 * 更新店铺
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop)throws ShopOperationException;
}
