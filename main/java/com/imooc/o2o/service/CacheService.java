package com.imooc.o2o.service;

public interface CacheService {

	/**
	 * 依据key前缀删除匹配该模式下的所有key-value
	 * 如：传入shopcategory_allfirstleve等都会被删除
	 * 以shopcategory打头的key-value都会被清空
	 */
	void removeFromCache(String keyPrefix);
	
}
