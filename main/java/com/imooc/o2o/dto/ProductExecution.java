package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Product;


import com.imooc.o2o.enums.ProductStateEnum;

public class ProductExecution {

		//结果状态
		private int state;
		
		//状态标识
		private String stateInfo;
		
		//商品数量
		private int count;
		
		//修改商品时使用（增删改查商品）
		private Product product;
		
		//获取product列表
		private List<Product> productList;
		
		
		
		public ProductExecution(){
			
		}

		/**
		 * 操作失败时使用的构造器
		 * 返回失败时的状态值和状态信息
		 * @param stateEnum
		 */
		public ProductExecution(ProductStateEnum stateEnum) {

			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
		}

		/**
		 * 操作商品执行成功时的构造器
		 * @param state
		 * @param stateInfo
		 * @param product
		 */

		public ProductExecution(ProductStateEnum stateEnum, Product product) {
			
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.product = product;
		}

		/**
		 * 操作成功时的构造器
		 * 返回成功时的状态值和状态信息，并且返回查询结果列表
		 * @param stateEnum
		 * @param ProductList
		 */
		
		public ProductExecution(ProductStateEnum stateEnum, List<Product> productList) {

			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.productList = productList;
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

		public Product getProduct() {
			return product;
		}

		public void setProduct(Product product) {
			this.product = product;
		}

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}
		
		
}
