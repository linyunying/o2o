package com.imooc.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;

//spring中的笔记，该注解代表该类为控制层并自动创建bean
@Controller
//springMVC中的笔记，前后端访问控制层通过以下路径名称访问
@RequestMapping("/superadmin")
public class AreaController {

	//日志文件的使用一般在controller和service层
	Logger logger=LoggerFactory.getLogger(AreaController.class);
	
	@Autowired
	private AreaService areaService;
	
	//指定访问该路径便是访问该方法,指定访问方法为get方法，get是通过URL传参
	@RequestMapping(value="/listarea",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listArea(){
		//日志开始
		logger.info("===start===");
		//日志开始时间
		long startTime=System.currentTimeMillis();
		
		/*
		 * 存放方法的返回值，如果service层方法出现错误则不会返回AreaList集合，
		 * 而是会返回错误信息，这时候我们需要定义一个map集合用户捕捉方法的放回类型
		 * hashmap中的key值不能重复，并且当下一个信息上来时会覆盖当前存放的信息
		 */
		Map<String,Object> modelMap=new HashMap<String,Object>();
		
		//方法返回的集合
		List<Area>list=new ArrayList<Area>();
		
		try{
			
			//如果运行正常，则方法返回的是list集合
			list=areaService.getAreaList();
			
			//当遇到下一个list对象的时候，会直接替换当前对象
		//有点类似循环遍历，但是该方法更快
			//将正常返回的集合添加进map集合当中
			modelMap.put("rows", list);
			//将集合对象的个数大小进行存错
			modelMap.put("total", list.size());
		}catch(Exception e){
			//如果方法返回发生错误。则输出错误信息
			e.printStackTrace();
			
			//如果成功，则无需输出
			modelMap.put("success", false);
			//遇到错误的时候，直接将错误放到集合当中，下次又发生错误则替换当前错误
			modelMap.put("errMsg", e.toString());
		}
		//日志测试错误信息
		logger.error("test error");
		//日志结尾时间
		long endTime=System.currentTimeMillis();
		//将结束时间和开始时间相减，然后传入到{}中
		logger.debug("costTime:[{}ms]",endTime - startTime);
		logger.info("===end===");
		return modelMap;
	}
}
