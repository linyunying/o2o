package com.imooc.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/**
	 * 通过账号密码获取信息
	 * 即登录账号密码获取用户的商铺信息
	 * 
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("username")String username,@Param("password") String password);

	/**
	 * 通过用户Id插叙对应的Localauth
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId")long userId);
	
	/**
	 * 添加平台账号
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

	/**
	 * 通过userId，username，password更改密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	
	int updateLocalAuth(@Param("userId")Long userId,@Param("username")String username,
			@Param("password")String password,@Param("newPassword")String newPassword,
			@Param("lastEditTime")Date lastEditTime
			);
}

