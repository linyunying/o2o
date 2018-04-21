package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {

	/**
	 * 根据账号，密码获取平台账号信息
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String username,String password);
	
	/**
	 * 根据userId获取平台账号信息
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);


	/**
	 * 绑定微信，生成平台专属的账号
	 * @param localAuth
	 * @return
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth)throws LocalAuthOperationException;


	/**
	 * 修改平台账号的登录密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newpassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId,String username,String password,String newpassword) throws LocalAuthOperationException;
	
}
