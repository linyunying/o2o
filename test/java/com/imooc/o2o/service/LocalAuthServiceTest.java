package com.imooc.o2o.service;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest extends BaseTest{

	@Autowired
	private LocalAuthService localAuthService;


	@Test
	@Ignore
	public void testABindLocalAuth(){
		//新增一条平台账号
		LocalAuth localAuth=new LocalAuth();
		PersonInfo personInfo=new PersonInfo();
		String username="testusername";
		String password="testpassword";
		///给平台账号设置上用户信息
		//给用户设置上用户ID，标明是某个用户创建的账号
		personInfo.setUserId(1l);
		//给平台账号设置用户信息，标明是与哪个用户绑定
		localAuth.setPersonInfo(personInfo);
		//设置账号
		localAuth.setUsername(username);
		//设置密码
		localAuth.setPassword(password);
		//绑定账号
		LocalAuthExecution lae=localAuthService.bindLocalAuth(localAuth);
		System.out.println(lae.getState());
		//通过userId找到新增的localAuth
		localAuth=localAuthService.getLocalAuthByUserId(personInfo.getUserId());
		//打印用户名字和账号密码看是否与预期一致
		System.out.println("用户昵称："+localAuth.getPersonInfo().getName());
		System.out.println("平台账号密码："+localAuth.getPassword());
		
	}
	
	@Test
	public void testBModifyLocalAuth(){
		//设置账号信息
		long userId=1;
		String username="testusername";
		String password="testpassword";
		String newpassword="testnewpassword";
		//修改该账号对应的密码
		LocalAuthExecution localAuthExecution=localAuthService.modifyLocalAuth(userId, username, password, newpassword);
		
		System.out.println(localAuthExecution.getState());
		
		//通过账号密码找到修改后的localAuth
		LocalAuth localAuth=localAuthService.getLocalAuthByUsernameAndPwd(username, newpassword);
		
		//打印用户名名字看看跟预期是否相符
		System.out.println(localAuth.getPersonInfo().getName());
	}
}
