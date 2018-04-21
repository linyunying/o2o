package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

	/**
	 * 首页列表页
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	private String index(){
		return "frontend/index";
	}
	
	
	/**
	 * 商品列表页路由
	 * @return
	 */
	@RequestMapping(value="/shoplist",method=RequestMethod.GET)
	private String shopList(){
		return "frontend/shoplist";
	}
	
	/**
	 * 店铺详情页面
	 */
	@RequestMapping(value="/shopdetail",method=RequestMethod.GET)
	private String shopdetail(){
		return "frontend/shopdetail";
	}
	
	/**
	 * 商品详情页
	 * 
	 */
	@RequestMapping(value="/productdetail",method=RequestMethod.GET)
	private String showProductDetail(){
		return "frontend/productdetail";
	}
}

