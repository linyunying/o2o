package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtill;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	 ProductDao  productDao;
	 
	@Autowired
	 ProductImgDao productImgDao;
	
	@Override
	@Transactional
	/**
	 * 1.处理缩略图，获取缩略图相对路径并赋值给product
	 * 2.往tb_product写入商品信息，获取productId
	 * 3.结合productId批量处理商品详情图
	 * 4.将商品详情图列表批量插入tb_product_img中
	 */
	public ProductExecution addProduct(Product product, ImgHolder thumbnail, List<ImgHolder> productImgHolderList)
			throws ProductOperationException {
		//空值判断
		if (product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			//给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//默认为上架状态
			product.setEnableStatus(1);
			//若商品缩略图不为空则添加
			if (thumbnail!=null) {
				addThumbnail(product,thumbnail);
			}
			try{
				//创建商品信息
				int effectedNum=productDao.insertProduct(product);
				
				if (effectedNum<=0) {
					throw new ProductOperationException("创建商品失败");
				}
			}catch (Exception e) {
				
				throw new ProductOperationException("创建商品失败"+e.toString());
			}
			
			//如果商品详情图不为空则添加
			if (productImgHolderList!=null&&productImgHolderList.size()>0) {
				addProductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		}else{
			//传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 */

	private void addThumbnail(Product product, ImgHolder thumbnail) {
		//获取图片绝对路径
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
	
		//获取图片相对路径
		String thumbnailAddr=ImageUtill.generateThumbnail(thumbnail, dest);
	
		//将缩略图地址添加进商品图片
		product.setImgAddr(thumbnailAddr);
		
	}
	
	/**
	 * 批量添加详情图片
	 * @param product
	 * @param productImgHolderList
	 */

	private void addProductImgList(Product product,List<ImgHolder> productImgHolderList ){
		//获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		
		//将用户传进来的详情图全部遍历出添加进另外一个集合添加进数据库
		
		//遍历图片一次去处理，并添加进product实体类里
		for (ImgHolder productImgHolder : productImgHolderList) {
			//调用图片工具类中的商品详情图工具获取图片相对路径
			String imgAddr=ImageUtill.generateNormalImg(productImgHolder, dest);
			
			ProductImg productImg=new ProductImg();
			
			productImg.setImgAddr(imgAddr);
			
			productImg.setProductId(product.getProductId());
			
			productImg.setCreateTime(new Date());
			
			productImgList.add(productImg);
			
		}
		
		//如果确实是有图片需要添加的，就执行批量添加操作
		if (productImgList.size()>0) {
			
			try {
				//将详情图片批量添加进数据库当中
				int effectedNum=productImgDao.batchInsertProductImg(productImgList);
				
				if (effectedNum<=0) {
					throw new ProductOperationException("商品详情图添加失败");
				}
				
			} catch (Exception e) {
				throw new ProductOperationException("商品详情图添加失败:"+e.toString());
			}
		}
	}

	/**
	 *根据商品id查询商品信息并返回该商品信息
	 */
	@Override
	public Product getProductById(long productId) {
		// TODO Auto-generated method stub
		return productDao.queryProductById(productId);
	}

	/**
	 * 修改商品信息以及修改商品图片，步骤如下：
	 * 1.若缩略图参数有值，则处理缩略图
	 * 若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	 * 2.若商品详情图列表有值，对商品详情图列表进行同样的操作
	 * 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
	 * 4.更新tb_product的信息
	 */
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImgHolder thumbnail, List<ImgHolder> productImgHolderList)
			throws ProductOperationException {
		//空值判断
		if (product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			//给商品设置上默认属性
			product.setLastEditTime(new Date());
			//若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加新的缩略图
			if (thumbnail!=null) {
				//先获取一遍原有信息，因为原来的信息里有图片地址
				Product temProduct=productDao.queryProductById(product.getProductId());
				//如果原来图片信息不为空则将其删除
				if (temProduct.getImgAddr()!=null) {
					ImageUtill.deleteFileOrPath(temProduct.getImgAddr());
				}
				//添加进新的缩略图
				addThumbnail(product, thumbnail);
			}
			//如果有新存入的商品详情图，则将原先的删除并添加新的图片
			if (productImgHolderList!=null&&productImgHolderList.size()>0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try{
				//更新商品信息
				int effectedNum=productDao.updateProduct(product);
				if (effectedNum<=0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);
			}catch (Exception e) {
				
				throw new ProductOperationException("更新商品信息失败："+e.toString());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 删除某个商品下的所有详情图片
	 * @param productId
	 */
	private void deleteProductImgList(Long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
		//删除原来的图片
		for (ProductImg productImg : productImgList) {
			ImageUtill.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有的图片信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	/**
	 * 查询商品列表并分页，可输入的条件有：
	 * 商品名（支持模糊查询），商品状态，店铺Id,商品类别
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		//页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//查询信息
		List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
		//基于同样的查询条件返回该查询条件下的商品总数
		int count=productDao.queryProductCount(productCondition);
		//将查询后的结构存储在另外一个类中
		ProductExecution pe=new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
