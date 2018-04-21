package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.HeadLine;

/**
 * 头条展示接口
 * @author Administrator
 *
 */
public interface HeadLineDao {

	/**
	 * 根据传入的查询条件（头条名查询头条）
	 * @param headLine
	 * @return
	 */
	List<HeadLine> queryHeadLine(@Param("headLineCondition")HeadLine headLine);
	
}
