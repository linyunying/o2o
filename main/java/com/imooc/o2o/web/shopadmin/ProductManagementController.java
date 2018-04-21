package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;


/**
 * 获取前后端商品类别数据交互
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	//支持上传商品详情图的最大数量
	private static final int IMAGEAXCOUNT=6;
	
	Map<String, Object> modelMap=new HashMap<String, Object>();
	
	
	/**
	 * 查询商品列表并分页，可输入的条件有：
	 * 商品名（支持模糊查询），商品状态，店铺Id,商品类别
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value="/getproductlistbyshop",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object>getProductListByShop(HttpServletRequest request ){
		//获取前台传过来的页码
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台传过来的每页要求返回的商品数上限，即每页最多显示多少条信息
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		//从当前session中获取店铺信息，主要是获取shopId
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
		//空值判断
		if ((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)) {
			/*
			 * 获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查找商品名去筛选某个店铺下的商品列表
			 * 筛选的条件可以进行排列组合
			 */
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
			//传入查询条件以及分页信息进行查询，返回相应商品列表以及总数
			ProductExecution pe=productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		
		return modelMap;
		
	}
	
	
	/**
	 * 将前台传进来的查询条件进行筛选整合排列
	 * 因为所有的查询条件都是整合到product类中然后直接将product对象传入接口中进行取出查询
	 * 所以需要将获取到的全部信息都进行整合在一起
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有指定类别的要求则添加进去
		if (productCategoryId!=-1L) {
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		//若有商品模糊查询的要求则添加进去
		if (productName!=null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}



	/**
	 * 修改商品信息及详情图片信息
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value="/modifyproduct",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>modifyProduct(HttpServletRequest request ){
	
		//判断是商品编辑时调用还是上下架操作的时候调用
		//若为前者则进行验证码判断，后者则跳过验证码判断
		boolean statusChange=HttpServletRequestUtil.getboolean(request, "statusChange");
		//验证码判断
		if (!statusChange&&!CodeUtil.checkVerifyCode(request)) {
			//验证码判断
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			
			return modelMap;
		}
		//接收前端参数的变量的初始化，包括商品，缩略图，详情图列表转为实体类
		ObjectMapper mapper=new ObjectMapper();
		
		Product product=null;
		
		ImgHolder thumbnail=null;
		List<ImgHolder> productImgList=new ArrayList<ImgHolder>();
		//图形解析器
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		//若请求中存在文件流，则取出相关文件（包括缩略图和详情图）
		try {
			if (multipartResolver.isMultipart(request)) {
				handleImage(request, productImgList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String productStr=HttpServletRequestUtil.getString(request, "productStr");
			//尝试获取前端传过来的表单String流并将其转换为Product实体类
			product=mapper.readValue(productStr,Product.class);
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		//非空判断
		if (product!=null) {
			try{
				//从session中获取当前店铺的id并赋值给product，减少对前端数据的依赖
				Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
			
				product.setShop(currentShop);
				//开始进行商品信息变更操作
				ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgList);
				if (pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		
		return modelMap;
		
	}

	/**
	 * 商品添加和编辑时的公共部分代码，用于处理传入的缩略图和详情图
	 * @param request
	 * @param productImgList
	 * @throws IOException
	 */
	private void handleImage(HttpServletRequest request, List<ImgHolder> productImgList) throws IOException {
		@SuppressWarnings("unused")
		ImgHolder thumbnail=null;
		MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
		//取出缩略图并创建ImgHolder对象
		CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("thumbnail");
		//如果取出的文件流不为空则进行创建对象
		if (thumbnailFile!=null) {
			thumbnail=new ImgHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
			
		}
		
		//取出详情图
		for(int i=0;i<IMAGEAXCOUNT;i++){
			CommonsMultipartFile productImgFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("productImg"+i);
			if (productImgFile!=null) {
				//若取出的第i个详情图文件流不为空，则将其加入 详情图列表
				ImgHolder productImg=new ImgHolder(productImgFile.getInputStream(), productImgFile.getOriginalFilename());
				
				productImgList.add(productImg);
			}else{
				//如果取出的第i个详情图为空，则终止循环
				break;
			}
		}
	}
	
	/**
	 * 根据商品id获取商品信息
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value="/getproductbyid",method=RequestMethod.GET)
	@ResponseBody
	//@RequestParam直接获取URL中的productId
	private Map<String, Object>getProductById(@RequestParam Long productId ){
		//非空判断
		if (productId>-1) {
		
			//获取商品信息
			Product product=productService.getProductById(productId);
		
			//获取该店铺下的商品类别列表
			//原因：将类别列表显示在前端，以供用户可选择修改商品类别
			List<ProductCategory> productCategoryList=productCategoryService.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","empty productId");
		}
		
		
		return modelMap;
		
	}
	
	
	/**
	 * 添加商品类别
	 */
	@RequestMapping(value="/addproduct",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>addProduct(HttpServletRequest request ){
		//验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类，将上传的信息转换为实体类
		ObjectMapper mapper=new ObjectMapper();
		
		Product product=null;
		//从前端获取信息，并将信息转换为字符串格式
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		//MVC接收的文件流用于接收前端的图片文件
		MultipartHttpServletRequest multipartHttpServletRequest=null;
		//将文件和文件名存储在一个类中
		ImgHolder thumbnail=null;
		//存储商品详情图
		List<ImgHolder> productImgList=new ArrayList<ImgHolder>();
		//图片解析器，获取session对象当中的上下文对象
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		
		try {
			//若请求中存在文件流，则取出相关文件(包括缩略图，详情图)
			if (multipartResolver.isMultipart(request)) {
				multipartHttpServletRequest=(MultipartHttpServletRequest)request;
				//取出缩略图并构建ImgHolder对象
				CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("thumbnail");
				//将获取到的缩略图片文件流存储在类中
				thumbnail=new ImgHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
			
				//取出详情图并且构建List<ImgHolder>列表对象，限制最多的详情图片一次只能上传6个
				for (int i = 0; i < IMAGEAXCOUNT; i++) {
					
					CommonsMultipartFile productImgFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("productImg"+i);
					
					if (productImgFile!=null) {
						//若取出的第i个详情图片文件流不为空，则将其加入详情图列表
					
						ImgHolder productImg=new ImgHolder(productImgFile.getInputStream(), productImgFile.getOriginalFilename());
						productImgList.add(productImg);
					}else {
						//如果取出的第i个详情图片文件流为空则终止 循环
						break;
					}
				}
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		
			return modelMap;	
		}
		
		try {
			//获取前端传过来的表单并将其转换为实体类
			product=mapper.readValue(productStr, Product.class);
			
		} catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.toString());
			return modelMap;
		}
		//若product信息，缩略图，详情图列表为非空，则楷书进行商品添加操作
		if (product!=null&&thumbnail!=null&&productImgList.size()>0) {
			try{
				//从session中获取当前店铺的id并赋值给product，减少对前端数据的依赖
			
				Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//执行添加操作
				ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
				if (pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		
		return modelMap;
		
	}
}
