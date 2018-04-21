package com.imooc.o2o.util;

/**
 * 路径工具类
 * 主要有两种路径
 * 1.依据执行环境的不同设定不同的根路径
 * 2.图片生成的子路径
 * @author Administrator
 *
 */
public class PathUtil {


	/**
	 * 1.依据运行环境的不同将图片进行保存
	 * 即如果该项目在Windows电脑中运行则将图片保存在E盘image文件目录下
	 * 当将项目移动到另外一台电脑进行运行并且这台电脑属性不是Windows
	 * 则将图片保存在home路径下
	 */

	//获取电脑文件路径分隔符\,字符串的key值是固定形式
	private static String separator=System.getProperty("file.separator");

	public static String getImgBasePath(){

		//获取电脑系统属性名称，字符串固定形式，不可自己命名
		String os=System.getProperty("os.name");

		//图片保存在不同电脑系统中的路径
		String basePath="";

		//测试返回的系统属性是否是以win为前缀进行开头
		/*
		 * 1.toLowerCase将字符串转为小写
		 * 2.startsWith是否以指定的字符串开头
		 */
		//如果电脑属性是Windows，则将图片保存在指定路径下
		if(os.toLowerCase().startsWith("."))
		{
			basePath="E:/image/linyunying/image";
			//若电脑是Linux系统或者其他系统则保存在另外路径中
		}else{
			basePath="E:/image/linyunying/image";
		}
		/*
		 * 因为代码中路径的斜杠与电脑中路径斜杠是相反的所以我们需要进行转换
		 */
		//将/分隔符转换为\分隔符才能将图片保存在电脑当中
		basePath=basePath.replace("/", separator);

		return basePath;
	}


	/**
	 * 店铺图片路径
	 * 一个店铺的图片分别存储在不同的子文件当中
	 */

	//返回图片相对路径
	public static String getShopImagePath(long shopId){

		String imagePath="/upload/item/shop/"+shopId+"/";

		return imagePath.replace("/", separator);
	}
}
