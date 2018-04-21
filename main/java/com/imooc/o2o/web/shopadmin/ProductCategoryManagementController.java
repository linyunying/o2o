package com.imooc.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;

/**
 * 获取前后端商品类别数据交互
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

	@Autowired
	ProductCategoryService productCategoryService;
	
	/**
	 * 删除商品类别
	 */
	@RequestMapping(value="/removeproductcategory",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> removeProductCategory(Long productCategoryId,HttpServletRequest request ){
		
		Map<String, Object> modelMap=new HashMap<String, Object>();
		
		if (productCategoryId!=null&&productCategoryId>0) {
			try{
				//获取会话当中的shopid 
				Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe=productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if (pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
						
			}catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","请至少选择一个商品类别");
		}
		return modelMap;
	}
	
	
	
	/**
	 * 批量添加商品类别信息
	 */
	
	@RequestMapping(value="/addproductcategorys",method=RequestMethod.POST)
	@ResponseBody
	//因为前段传递过来的list集合的商品类别信息，所以参数加上@RequestBody
	/**
	 * @RequestBody注解是可以自动将前台的信息赋值给实体类属性值。
	 * 前提是前端的JSON字符串的key必须对应后端实体类的属性名
	 *简单来说，就是前台添加商品类别信息后传递给后端会直接为ProductCategory实体类对应的属性赋值，然后添加到集合当中
	 */
	private Map<String, Object>addProductCategorys(@RequestBody List<ProductCategory>productCategoryList,HttpServletRequest request){
		
		Map<String , Object> modelMap=new HashMap<String,Object>();
	
		//因为我们添加的商品类别需要指定是在哪个店铺下进行添加，所以需要从session会话当中获取shopId
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");

		/*
		 * 需要给集合中的每个ProductCategory对象指定ShopId,
		 * 这样我们才能指定是为哪个店铺进行添加商铺类别信息
		 * 将session对象中的shop实体类取出shopId进行赋值
		 * 
		 */
		for(ProductCategory pc:productCategoryList){
			pc.setShopId(currentShop.getShopId());
		}
		
		//如果传入的商品类别信息有效，则将商品类别信息传进批量添加进数据库当中
		if (productCategoryList!=null&&productCategoryList.size()>0) {
			try{
				ProductCategoryExecution pe=productCategoryService.bathAddtProductCategory(productCategoryList);
				//如果传来的状态值是成功的则返回给前端成功信息
				if (pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else{
					//如果传入失败，则返回失败信息
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
				
			}catch (RuntimeException e) {
				// TODO: handle exception
				//捕获异常信息并返回给前端
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
			//如果传入的商品信息少于1条则返回对应的警告信息给前端
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别");
		}
		return modelMap;
	}
	
	
	/**
	 * 获取某个店铺下的所有商品分类信息
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value="/getproductcategorylist",method=RequestMethod.GET)
	@ResponseBody
	//Result和Map作用类似，只不过对JSON进一层封装
	private Result<List<ProductCategory>>getProductCategoryList(HttpServletRequest request){
		
		/*
		 * 模拟某个商铺的ID
		 * 然后根据id获取该商铺下的所有商品分类信息
		 */
		Shop shop=new Shop();
		
		shop.setShopId(1l);

		//将shop放进session对象当中
		request.getSession().setAttribute("currentShop", shop);
		
		//获取之前进入到管理页面的时候保存在session对象当中的shop实体类，里面含shopId
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
		
		List<ProductCategory> list=null;
		//如果获取到的商铺信息有效
		if (currentShop!=null&&currentShop.getShopId()>0) {
			
			//根据商铺id查询出所有的商品列表
			list=productCategoryService.getProductCategoryList(currentShop.getShopId());
			//然后将商品信息直接返回给前台
			return new Result<List<ProductCategory>>(true, list);
		}else{
			//如果商铺信息无效，则输出错误信息
			ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
			
			//将错误状态值和对应的错误信息返回给前端
			return new Result<>(false, ps.getState(), ps.getStateInfo());
			
		}

	}
	
	
	
	
}
