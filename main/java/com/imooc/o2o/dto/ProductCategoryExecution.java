package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;

/**
 * 商品类别状态值
 * @author Administrator
 *
 */
public class ProductCategoryExecution {

	//结果状态
	private int state;
	
	//状态标识
	private String stateInfo;
	
	//商品类别集合
	
	private List<ProductCategory> productCategoryList;
	
	public ProductCategoryExecution(){
		
	}

	/**
	 * 操作失败时使用的构造器
	 * 返回失败时的状态值和状态信息
	 * @param stateEnum
	 */
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {

		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	
	/**
	 * 操作成功时的构造器
	 * 返回成功时的状态值和状态信息，并且返回查询结果列表
	 * @param stateEnum
	 * @param productCategoryList
	 */
	
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum, List<ProductCategory> productCategoryList) {

		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.productCategoryList = productCategoryList;
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

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
	
	
}
