package com.imooc.o2o.entity;

import java.util.Date;

/**
 * 用户实体类
 * 用户包括买家，商家，以及超级管理员
 * 超级管理员管理着买家与卖家。
 * 也就是该网站超级管理员是统筹者
 * @author Administrator
 *
 */

public class PersonInfo {

	/**
	 * 用户ID
	 * 可能会有多个用户进行注册故采用long型
	 */
	private Long userId;
	/**
	 * 用户姓名
	 */
	private String name;
	/**
	 * 用户图片地址
	 */
	private String profileImg;
	/**
	 * 用户邮箱
	 */
	private String email;
	/**
	 * 用户性别
	 */
	private String gender;
	/**
	 * 用户状态
	 * 用于验证用户是否有资格登录商城进行操作
	 * 0.代表不能登录商城	1.代表能够登录商城
	 */
	private Integer enableStatus;
	/**
	 * 用户身份标识
	 * 即标明该用户是买家、商家还是超级管理员
	 * 1.代表顾客		2.代表店家	3.代表超级管理员
	 */
	private Integer userType;
	/**
	 * 用户创建时间
	 */
	private Date createTime;
	/**
	 * 用户修改时间
	 */
	private Date lastEditTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	
	
}
