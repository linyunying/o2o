package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * 通过网址/o2o/shopadmin/xxx.html访问对应的HTML网页
 * 完整的访问网址如下：/o2o/WEB-INF/html/shoplist.html
 * 但是我们在web.xml中的视图解析器中将头尾直接写上，这样访问一个页面的时候会自动加上
 * 头部/WEB-INF/html/和尾部.html
 * 所以我们只需要通过shopadmin/shoplist即可访问对应的html页面
 * @author Administrator
 *
 */


@Controller
@RequestMapping(value="shopadmin",method={RequestMethod.GET})
public class ShopAdminController {

	/**
	 * 访问店铺注册页面和修改店铺信息页面
	 * @return
	 */
	@RequestMapping(value="/shopoperation")
	public String shopOperation(){
		return "shop/shopoperation";
	}
	
	/**
	 * 访问商铺列表页面
	 * @return
	 */
	@RequestMapping(value="/shoplist")
	public String shopList(){
		return "shop/shoplist";
	}
	
	/**
	 * 访问商铺管理页面
	 * @return
	 */
	@RequestMapping(value="/shopmanagement")
	public String productCategoryList(){
		return "shop/shopmanagement";
	}
	
	/**
	 * 访问商品分类管理页面
	 * @return
	 */
	@RequestMapping(value="/productcategorymanagement",method=RequestMethod.GET)
	public String shopManagement(){
		return "shop/productcategorymanagement";
	}
	
	/**
	 * 访问商品添加/编辑页面
	 * @return
	 */
	
	@RequestMapping(value="/productoperation")
	public String productOperation(){
		//转发至商品添加/编辑页面
		return "shop/productoperation";
	}
	
	/**
	 * 访问商品管理页面
	 */
	
	@RequestMapping(value="/productmanagement")
	public String productmanagement(){
		//转发至商品添加/编辑页面
		return "shop/productmanagement";
	}
}
