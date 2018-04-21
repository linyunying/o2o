package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{

	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Autowired
	ProductDao  productDao;
	
	/**
	 * 根据商铺id查询商品类别
	 */
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		// TODO Auto-generated method stub
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	/**
	 * 批量增加商品类别信息
	 */
	@Override
	//添加事物回滚
	@Transactional
	public ProductCategoryExecution bathAddtProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		/*
		 * 先判断用户是否有添加进商品类别信息，至少添加一条
		 */
		
		if (productCategoryList!=null&&productCategoryList.size()>0) {
			
			try{
				//将用户添加的信息添加进数据库当中
				int effectedNum=productCategoryDao.bathInsertProductCategoryList(productCategoryList);
				//如果影响数据库影响行数小于等于0，则说明数据库操作失败
				if (effectedNum<=0) {
					throw new ProductCategoryOperationException("商品类别创建失败！！");
				}else{
					//如果操作成功，则返回操作成功的状态值和状态信息及商品类别信息赋值
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategoryList);
				}
				//如果插入失败则捕获异常
			}catch (Exception e) {
				
				throw new ProductCategoryOperationException("bathAddtProductCategory error:"+e.getMessage());
			}
		}else{
			//如果用户添加商品类别少于1则返回错误状态值
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	/**
	 * 删除商品类别
	 */
	@Override
	//因为操作数据库
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException{
		
		//将此商品类别下的商品的类别id置为空
		try{
			int effectedNum=productDao.updateProductCategoryToNull(productCategoryId);
			if (effectedNum<0) {
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		}catch (Exception e) {
			// TODO: handle exception
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
		
		try{
			//删除数据库影响行数
			int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			//如果影响行数小于等于0则代表操作数据库失败
			if (effectedNum<=0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
				
				//如果成功则返回商品删除成功的状态
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch (Exception e) {
			//捕捉异常
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
		

	}

}
