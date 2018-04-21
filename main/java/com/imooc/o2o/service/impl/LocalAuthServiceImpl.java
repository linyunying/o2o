package com.imooc.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;


@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	LocalAuthDao localAuthDao;
	
	/**
	 * 根据账号密码获取平台信息
	 */
	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
		// TODO Auto-generated method stub
		/**
		 * 对密码进行加密
		 */
		return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
	}

	/**
	 * 根据userId获取平台信息
	 */
	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserId(userId);
	}

	/**
	 * 绑定微信，生成平台专属账号
	 */
	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// TODO Auto-generated method stub
		
		//空值判断，传入的localAuth账号，密码，用户信息，特别是userId不能为空，否则直接返回错误
		if (localAuth==null||localAuth.getPassword()==null|| localAuth.getUsername()==null
				||localAuth.getPersonInfo()==null||localAuth.getPersonInfo().getUserId()==null) {
			
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		//查询此用户是否已经绑定过平台账号
		LocalAuth tempAuth=localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if (tempAuth!=null) {
			//如果绑定过则直接退出，以保证平台账号的唯一性
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			//如果之前没有绑定过平台账号，则创建一个平台账号与该用户绑定
			localAuth.setCreateTime(new Date());

			localAuth.setLastEditTime(new Date());
			//对密码进行MD5加密
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			
			int effectedNum=localAuthDao.insertLocalAuth(localAuth);
			//判断创建是否成功
			if (effectedNum<=0) {
				throw new LocalAuthOperationException("账号绑定失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
	
			throw new LocalAuthOperationException("insertLocalAuth error:"+e.getMessage());
		}
	}

	/**
	 * 修改平台账号的登录密码
	 */
	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newpassword)
			throws LocalAuthOperationException {
		//非空判断，判断传入的用户Id，账号，新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
		if(userId!=null&&username!=null&&password!=null&&newpassword!=null&&!password.equals(newpassword)){
			try {
				
				//更新密码，并对新密码进行MD5加密
				int effectedNum=localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newpassword), new Date());
				//判断是否更新成功
				if (effectedNum<=0) {
				
					throw new LocalAuthOperationException("更新密码失败");
					
				}
				//否则返回更新密码成功
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				
			} catch (Exception e) {
				throw new LocalAuthOperationException("更新密码失败："+e.toString());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}
}
