package com.imooc.o2o.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtill;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
/**
 * 插入店铺信息和店铺图片地址文件流的实现类
 * @author Administrator
 *
 */
@Service
public class ShopServiceImpl implements ShopService {

	//自动注入shopDao实例
	@Autowired
	private ShopDao shopDao;

	@Override
	//添加事物回滚注解，说明该方法需要执行事物回滚操作
	@Transactional
	public ShopExecution addShop(Shop shop, ImgHolder thumbnail) throws ShopOperationException{

		/*
		 * 如果传入的商品信息为空，则返回添加商品失败的构造函数
		 */
		if(shop==null){

			//如果商铺信息为空，则传入枚举值
			return  new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		/**
		 * 在执行商铺信息添加的时候，可能会出现某条信息出现错误，如填写信息不规范，
		 * 这时候就必须要执行事务回滚，不能将错误信息添加进数据库当中
		 */
		try{
			/*
			 * 为这三个属性赋值的原因：
			 * 
			 * 店铺插入成功的时候，立马为店铺状态赋值和店铺创建和操作时间赋值
			 * 就是当我们提交店铺信息的时候，会成为审核中的状态
			 */
			//商铺初始状态都是审核中
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());

			//获取操作数据库的影响行数
			int effectedNum=shopDao.insertShop(shop);

			//如果影响行数<=0则说明操作数据库失败
			if(effectedNum<=0){
				throw new ShopOperationException("店铺创建失败");
				//操作数据库成功，则判断商铺图片地址是否为空
			}else{
				if(thumbnail.getClass()!=null){
					/*
					 * 通过addshopImg函数传入shop对象和shop图片地址
					 * 因为必须先行传入商铺信息并且商铺信息上传成功后才能传入商铺地址，
					 * 该函数类似shop.setShopImg(shopImg);为图片地址赋值
					 */
					//插入图片地址方法
					try{

						addShopImg(shop,thumbnail);
					}catch(Exception e){
						throw new ShopOperationException("addShopImg error:"+e.getMessage());
					}

				}
				//更新店铺的图片地址，修改图片地址
				effectedNum=shopDao.updateShop(shop);
				
				if(effectedNum<=0){
					throw new ShopOperationException("更新图片地址失败");
				}
			}

		}catch(Exception e){
			/**ShopOperationException继承了RuntimeException
			 * 在事物回滚操作中使用RuntimeException的原因：
			 * 1.如果使用exception获取异常信息，
			 * 则添加店铺时如果一条信息出错在控制台报错，
			 * 在数据库信息中还是会添加进其余没有出错的店铺的信息。
			 * 2.使用ShopOperationException获取添加信息错误，则只要商铺添加信息有一条出错，则其余信息也不能成功添加进数据当中。
			 * 换句话来说就是会出现事务回滚，操作数据库的时候要么一起失败要么一起成功
			 * 
			 */
			//抛出RuntimeException异常
			throw new ShopOperationException("addShop error:"+e.getMessage());
		}

		/*
		 * 创建成功则返回店铺是审核中状态
		 */
		return  new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	/**
	 * 根据传入的shop对象和shop图片地址，
	 * 然后获得图片的相对路径上传到扇商铺信息当中
	 * @param shop已经传入信息的商铺对象
	 * @param shopImg上传到商铺信息的图片路径
	 */
	private void addShopImg(Shop shop,ImgHolder thumbnail){
		//获取shop图片目录的相对路径/upload/item/shop/shopId/
		String dest=PathUtil.getShopImagePath(shop.getShopId());
		//将相对路径图片传进去和图片的相对路径
		String shopImgAddr=ImageUtill.generateThumbnail(thumbnail,dest);

		//为商铺图片传入相对路径信息
		shop.setShopImg(shopImgAddr);
		
		
		
		
	}
	/**
	 * 根据shopId获取店铺信息传递给前端，
	 * 当用户想修改店铺信息的时候可以根据之前的店铺信息进行修改
	 */
	@Override
	public Shop getQueryByShopId(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}
	
	/**
	 * 修改店铺信息和店铺图片
	 * shop:前端用户修改后的店铺信息
	 */
	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, ImgHolder thumbnail) throws ShopOperationException {
	
		//判断店铺信息是否为空并且判断修改的店铺信息是否为空，我们需要根据shopId才能找到对应的商铺信息进行修改
		if(shop==null||shop.getShopId()==null){
			//如果为空则抛出店铺为空的错误
			
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			//1.判断用户是否更换图片,并且图片名称不能为空或者空字符串
			try{
			if(thumbnail.getImage()!=null&&thumbnail.getImageName()!=null&&!"".equals(thumbnail.getImageName())){
				
				//根据id查询店铺信息
				Shop temShop=shopDao.queryByShopId(shop.getShopId());
				//如果查询出来的店铺信息中的店铺图片不为空，则将原来的图片信息删除
				if (temShop.getShopImg()!=null) {
					//删除原来商铺信息中的图片信息
					ImageUtill.deleteFileOrPath(temShop.getShopImg());
					
				}
				/*
				 * 传入shop的原因：
				 * 因为查询出来的temShop还是之前没被用户修改的商铺的信息
				 * 从前端传来的shop对象是用户已经进行更改的店铺信息
				 * 将新图片地址设置在该对象中，然后直接进行修改店铺信息
				 */
				//删除完毕将用户新上传的图片添加信息商铺信息
				addShopImg(shop,thumbnail);

			}
			
			//2.更新店铺信息
			shop.setLastEditTime(new Date());
			
			//操作数据库的影响行数
			int effectedNum=shopDao.updateShop(shop);
			//如果操作失败则抛出异常
			if (effectedNum<=0) {
				
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
				
			}else {
				shop=shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS,shop);
			}}catch (Exception e) {
				throw new ShopOperationException("modifyShop err:"+e.getMessage());
			}
		}
		
	
	}
	
	/**
	 pageIndex:
	 	前端返回的当前页码数（如第一页，第二页），数据库当中是以条数开始，如从0条开始，然后需要查询几条
	 * 所以我们需要公式将前端当前页码数转为后端SQL语句分页查询所需要的条数
	 * 将当前页码数转为条数公式：(当前页数-1)*pageSize(当前页的行数,即一页当中有几条店铺信息)
	 *pageSize:
	 *当前页共几行
	 */
	@Override
	public ShopExecution getQueryShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//将前端页数转为后端所需要的条数
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		
		//根据信息查询并指定当前页可显示的商铺数量，如果超出则进行分页查询
		List<Shop> shopList=shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		
		//根据信息查询出所有的店铺数量结果
		int count=shopDao.queryShopCount(shopCondition);
		//店铺的执行状态
		ShopExecution se=new ShopExecution();
		
		//如果根据信息查询的店铺结果不为空
		if(shopList!=null){
			//将店铺数量添加进se对象当中
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			//如果查询没有结果，则返回商铺状态结果
			se.setState(ShopStateEnum.NULL_SHOP.getState());
		}
		
		return se;
	}


}
