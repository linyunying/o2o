package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

/**
 * 店铺执行的的返回状态
 * @author Administrator
 *
 */
public class ShopExecution {

	//结果状态
	private int  state;
	
	//状态标识
	private String stateInfo;
	
	//店铺数量
	private int count;
	
	//操作的shop（增删改店铺的时候用到）
	
	private Shop shop;
	
	//shop列表（查询店铺列表的时候使用）
	
	private List<Shop> shopList;
	
	public ShopExecution(){
		
	}
	
	/*
	 * 引用枚举类，枚举类中定义的是店铺状态值
	 * 该构造函数是指店铺状态
	 */

	//店铺操作失败的时候使用的构造器
public ShopExecution(ShopStateEnum stateEnum){
	
	/*
	 * 店铺状态值
	 * 
	 */
	//获取整型的状态值
	this.state=stateEnum.getState();
	
	//获取字符型的状态值
	this.stateInfo=stateEnum.getStateInfo();
	}

//店铺操作成功的时候使用的构造器
public ShopExecution(ShopStateEnum stateEnum,Shop shop){
	
	/*
	 * 店铺状态值
	 * 
	 */
	//获取整型的状态值
	this.state=stateEnum.getState();
	
	//获取字符型的状态值
	this.stateInfo=stateEnum.getStateInfo();
	
	this.shop=shop;
}

//店铺操作成功的时候使用的构造器
public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList){
	
	/*
	 * 店铺状态值
	 * 
	 */
	//获取整型的状态值
	this.state=stateEnum.getState();
	
	//获取字符型的状态值
	this.stateInfo=stateEnum.getStateInfo();
	
	this.shopList=shopList;
}

public int getState() {
	return state;
}

public void setState(int state) {
	this.state = state;
}

public String getStateInfo() {
	return stateInfo;
}

public void setStateInfo(String stateInfo) {
	this.stateInfo = stateInfo;
}

public int getCount() {
	return count;
}

public void setCount(int count) {
	this.count = count;
}

public Shop getShop() {
	return shop;
}

public void setShop(Shop shop) {
	this.shop = shop;
}

public List<Shop> getShopList() {
	return shopList;
}

public void setShopList(List<Shop> shopList) {
	this.shopList = shopList;
}


} 
