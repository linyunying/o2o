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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImgHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;


/**
 * 实现前后台数据交互
 * 实现店铺从前端传递值过来的逻辑
 * @author Administrator
 *
 */
@Controller
//访问路径
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	ShopService shopService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private AreaService areaService;

	private static 	Map<String, Object> modelMap=new HashMap<String, Object>();




	/**
	 * 
	 * 六、
	 * getShopManagementInfo函数
	 * 
	 * 店铺管理页面，先等下到时候做完前端再来做笔记
	 */
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		//从用户请求当中获取shopId（单击<进入>超链接时会有进入商铺管理页面的shopId）
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		//如果用户请求当中没有shopId则从session会话当中获取
		if(shopId<=0){
			//从当前URL当中的session对象获取shopid
			Object currentShopObj=request.getSession().getAttribute("currentShop");

			//如果从session对象中还是没获取id则返回之前的店铺列表页面
			if (currentShopObj==null) {

				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");

			}else{
				//如果获取到shopId则说明进入商铺管理页面成功，并将shopId返回前端，以便进入商铺信息页面
				Shop currentShop=(Shop)currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
			/**
			 *  * 如果前端有传入Id则直接访问到商铺管理页面
			 * 前端传入	var shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId=' + shopId;
			 * 如果shopId有效则将shopId保存在session对象当中
			 * 然后将redirect设为false，直接进入管理店铺页面

			 */
		}else {
			Shop currentShop=new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}



	/**
	 * 五、
	 * 根据用户信息查询该用户所有的店铺列表信息
	 * 进行分页查询店铺信息
	 */
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopList(HttpServletRequest request){

		/**
		 * 模拟用户登录
		 */
		/*
		PersonInfo user=new PersonInfo();

		user.setUserId(1l);

		user.setName("模拟用户");

		//将用户信息保存在session当中，当我们进行登录的时候直接通过session对象获取到用户信息及对应的所有店铺
		request.getSession().setAttribute("user", user);
*/
		//用户进行登录操作时，直接从session对象中获取信息
		PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
		try {

			/**
			 * 进行模拟用户登录操作，当用户登录则将信息传入到service进行处理
			 * 然后将处理结果直接取出放进集合返回给前端
			 * 并且将用户姓名一起返回，显示查询出的店铺属于哪个店家
			 */
			Shop shopCondition=new Shop();

			shopCondition.setOwner(user);

			ShopExecution se=shopService.getQueryShopList(shopCondition, 0, 50);

			modelMap.put("shopList", se.getShopList());
			//列出店铺成功之后，将店铺放入session中作为权限验证依据，即该账号只能操作它自己的店铺
			request.getSession().setAttribute("shopList", se.getShopList());
			modelMap.put("user",user);
			modelMap.put("success", true);

		} catch (Exception e) {

			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;


	}

	/**
	 * 四、
	 * getShopById函数
	 * 根据前端用户传递过来的店铺id查询出店铺信息并把店铺信息以JSON形式返回给前端
	 */
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopById(HttpServletRequest request){

		//获取前端店铺ID，并将字符串ID转换为Long长整型
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");

		if(shopId>-1){
			try{
				//根据id获取商铺信息
				Shop shop=shopService.getQueryByShopId(shopId);

				//将全部区域信息返回给前端，前端用于修改区域，之所以不列出商铺类别信息是因为一旦用户注册的时候就不允许进行更改
				List<Area> areaList=areaService.getAreaList();

				//将查询出的店铺信息和区域列表添加进集合返回给前端
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);

			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}

		}
		return modelMap;

	}


	/**
	 * 三、
	 * getShopInitInfo函数
	 * 
	 * 返回给店铺信息列表所需要的商铺类别和区域
	 */

	/*
	 * 商铺类别和区域信息
	 */

	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)

	/**
	 * 以JSON格式返回商铺类别和区域信息给前端下拉框中选择
	 * @return
	 */
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){


		//存储查询的商铺类别
		List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
		//存储查询出的区域信息
		List<Area> areaList=new ArrayList<Area>();

		try{
			shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());

			areaList=areaService.getAreaList();

			/*
			 * 将商铺分类信息集合以及区域集合添加进map集合当中
			 */
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);

		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}


	/**
	 * 二、
	 * registerShop函数
	 * 注册店铺信息
	 * 
	 * @param request
	 * @return
	 */

	/*
	 * 店铺的注册，获取从前端传递过来的值转为shop实体类的相关参数
	 * 接收并返回一些结果信息
	 * HttpServletRequest代表的是从前端传递过来的参数都存储在该类中
	 */
	//访问路径以及从前端传递过来的访问方法是post,从表单传递过来的方法是post
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	//将返回的结果用JSON显示
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		/**
		 * 将用户验证码的判断结果返回给前端
		 */
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", true);
			modelMap.put("errMsg", "输入了错误的验证码");

			return modelMap;
		}
		/*
		 * 获取前端传递过来的信息转为字符串
		 */
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");

		/*
		 * 将JSON字符串转为实体类
		 */
		ObjectMapper mapper=new ObjectMapper();

		Shop  shop=null;

		try{
			//转为实体类


			shop=mapper.readValue(shopStr, Shop.class);


		}catch(Exception e){
			/*
			 * 判断前端是否成功调用后端方法
			 * 
			 * 返回前台执行操作结果
			 * 当前端操作失败时，则success返回false
			 * 操作成功时返回true
			 * 
			 * 当success的value值为false时，则说明将商铺信息转为实体类失败
			 * 添加错误信息返回给前端
			 */
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());

			//返回执行结果和错误信息
			return modelMap;
		}
		/**
		 * 采用spring实现文件上传原理
		 * 1. 图片一上传，图片解析器CommonsMultipartResolver就会解析保存在request当中的图片
		 * 2.判断图片是否上传，上传解析后的图片转换为MultipartHttpServletRequest
		 */

		/*
		 * 采用图片解析器解析上传到request当中的图片
		 */

		CommonsMultipartResolver commonsMultipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());//获取当前会话的上下文路径

		/**
		 * 转换图片，当用户上传图片时，
		 */

		/*
		 * 关于 CommonsMultipartFile
		 * 
		 * 1.spring有个特点，就是前台传上来的文件会以 MultipartFile接口进行处理
		 * 而CommonsMultipartFile实现类进行实现
		 * 2.但是CommonsMultipartFile这个工具类虽然好用
		 * 但是有个缺点：目前没有好的方法将CommonsMultipartFile实现类进行初始化，
		 * 而只能通过前台文件上传控件将他的文件流传送过来才能进行初始化
		 */
		CommonsMultipartFile shopImg=null;


		/*
		 * 判断用户是否上传图片到request对象当中
		 */

		if(commonsMultipartResolver.isMultipart(request)){
			/*
			 * 将request请求转换为MultipartHttpServletRequest请求
			 */

			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;

			/*
			 *获取前端上传文件
			 *通过MultipartHttpServletRequest请求转换为CommonsMultipartFile获取上传的文件
			 * "shopImg"是文件要提交的表单的name值
			 */
			shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");

			return modelMap;
		}

		//2.注册店铺

		/*
		 * 判断商铺信息和文件为空是否为空
		 * 
		 */
		if(shop!=null&&shopImg!=null){

			/*
			 *根据session会话获取用户信息 
			 */
			/**
			 * 店家修改店铺信息之前是需要登录操作
			 * 我们根据店家登录的获取店家信息
			 */
			/*
			 * 店主信息
			 */
			PersonInfo owner=(PersonInfo)request.getSession().getAttribute("user");

			/*
			 * 将店主信息传入到商铺信息当中
			 * 判断该商铺属于哪个店家
			 */
			shop.setOwner(owner);

			/*
			 * 添加商铺信息和商铺图片后返回商铺状态
			 * 刚开始提交的上去的商铺状态都是审核中
			 * 即state=0，stateinfo="审核中"
			 */
			/*
			 * 直接获取文件名，不需要传入file文件
			 * 图片采用inputStream进行传输
			 */
			ShopExecution se;
			try {

				ImgHolder imgHolder=new ImgHolder(shopImg.getInputStream(), shopImg.getName());

				se = shopService.addShop(shop,imgHolder);


				//如果返回状态与枚举类中的的审核状态一致，则说明调用成功
				if(se.getState()==ShopStateEnum.CHECK.getState()){

					modelMap.put("success", true);
					/*
					 * 一个用户可能会有多个商铺。我们可以通过session会话列出用户可以操作的店铺列表
					 * 
					 */
					@SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
					//如果这是用户第一家店的话
					if (shopList==null||shopList.size()==0) {

						shopList=new ArrayList<Shop>();
					}
					//如果不为空则将店铺添加进集合当中
					shopList.add(se.getShop());
					//将用户的所有店家保存在session会话中
					request.getSession().setAttribute("shopList", shopList);

				}else{
					modelMap.put("success", false);
					/**
					 * 如果返回的商铺状态与枚举类中的不一致，则返回其他状态说明为什么出错
					 */
					modelMap.put("errMsg",se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}

			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");

			return modelMap;
		}

	}

	/**
	 * 一、
	 * modifyShop函数
	 * 
	 * 修改店铺信息并返回结果
	 * @param request
	 * @return
	 */
	//访问路径以及从前端传递过来的访问方法是post,从表单传递过来的方法是post
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	//将返回的结果用JSON显示
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {


		/**
		 * 将用户验证码的判断结果返回给前端
		 */
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", true);
			modelMap.put("errMsg", "输入了错误的验证码");

			return modelMap;
		}
		/*
		 * 获取前端传递过来的信息转为字符串
		 * 不包含图片信息
		 */
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");

		/*
		 * 将JSON字符串转为实体类
		 */
		ObjectMapper mapper=new ObjectMapper();

		Shop  shop=null;

		try{
			//转为实体类
			shop=mapper.readValue(shopStr, Shop.class);

		}catch(Exception e){
			/*
			 * 判断前端是否成功调用后端方法
			 * 
			 * 返回前台执行操作结果
			 * 当前端操作失败时，则success返回false
			 * 操作成功时返回true
			 * 
			 * 当success的value值为false时，则说明将商铺信息转为实体类失败
			 * 添加错误信息返回给前端
			 */
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());

			//返回执行结果和错误信息
			return modelMap;
		}

		/**
		 * 采用spring实现文件上传原理
		 * 1. 图片一上传，图片解析器CommonsMultipartResolver就会解析保存在request当中的图片
		 * 2.判断图片是否上传，上传解析后的图片转换为MultipartHttpServletRequest
		 */

		/*
		 * 采用图片解析器解析上传到request当中的图片
		 */

		CommonsMultipartResolver commonsMultipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());//获取当前会话的上下文路径

		/**
		 * 转换图片，当用户上传图片时，
		 */

		/*
		 * 关于 CommonsMultipartFile
		 * 
		 * 1.spring有个特点，就是前台传上来的文件会以 MultipartFile接口进行处理
		 * 而CommonsMultipartFile实现类进行实现
		 * 2.但是CommonsMultipartFile这个工具类虽然好用
		 * 但是有个缺点：目前没有好的方法将CommonsMultipartFile实现类进行初始化，
		 * 而只能通过前台文件上传控件将他的文件流传送过来才能进行初始化
		 */
		CommonsMultipartFile shopImg=null;


		/*
		 * 判断用户是否上传图片到request对象当中
		 */

		if(commonsMultipartResolver.isMultipart(request)){
			/*
			 * 将request请求转换为MultipartHttpServletRequest请求
			 */

			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;

			/*
			 *获取前端上传文件
			 *通过MultipartHttpServletRequest请求转换为CommonsMultipartFile获取上传的文件
			 * "shopImg"是文件要提交的表单的name值
			 */
			shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}

		/**
		 * 传入修改后的店铺信息和店铺图片
		 */

		/*
		 * 判断商铺信息和商铺id不为空
		 * 
		 */
		if(shop!=null&&shop.getShopId()!=null){


			/*
			 * 添加商铺信息和商铺图片后返回商铺状态
			 * 刚开始提交的上去的商铺状态都是审核中
			 * 即state=0，stateinfo="审核中"
			 */
			/*
			 * 直接获取文件名，不需要传入file文件
			 * 图片采用inputStream进行传输
			 */
			ShopExecution se;
			try {
				/**
				 * 用户不一定会修改图片信息，所以图片信息可能为空
				 */
				if(shopImg==null){

					se=shopService.modifyShop(shop,null);
				}else {

					ImgHolder imgHolder=new ImgHolder(shopImg.getInputStream(), shopImg.getName());
					//将
					se = shopService.modifyShop(shop, imgHolder);

				}


				//如果返回状态与枚举类中的的审核状态一致，则说明调用成功
				if(se.getState()==ShopStateEnum.SUCCESS.getState()){

					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					/**
					 * 如果返回的商铺状态与枚举类中的不一致，则返回其他状态说明为什么出错
					 */
					modelMap.put("errMsg",se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}

			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺ID");

			return modelMap;
		}

	}


}
