package com.imooc.o2o.entity;

import java.util.Date;

/**
 * 区域实体类
 * @author Administrator
 *
 */
public class Area {

	/**
	 * 区域ID
	 * 如果采用int类型的话
	 * 当我们值为空的时候该属性默认值为0
	 * 我们不希望默认值为0的话就采用Integer
	 * 这样属性值为null就为null，不会默认为0
	 */
	private Integer areaId;
	/**
	 * 区域名称
	 */
	private String areaName;
	/**
	 * 区域权重
	 * 即优先级
	 * 权重越大排列越靠前
	 */
	private Integer priority;
	/**
	 * 区域创建时间
	 */
	private Date createTime;
	/**
	 * 区域更新时间
	 */
	private Date lastEditTime;
	
	
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
