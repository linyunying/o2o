package com.imooc.o2o.util;



import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImgHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 图片处理工具类
 * 详情见网址http://www.cnblogs.com/huhx/p/thumbnailator.html
 * 
 * @author Administrator
 *
 */
public class ImageUtill {


	//日期格式化
	private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");

	//随机数
	private static final Random r=new Random();

	//创建日志信息，捕捉路径
	private static Logger logger=LoggerFactory.getLogger(ImageUtill.class);

	/**
	 * 调用generateThumbnail函数之前需要
	 * 将CommonsMultipartFile转化为File
	 * @param cFile
	 * @return
	 */

	/*
	 * 关于 CommonsMultipartFile转化为File的解答：
	 * 
	 * 1.spring有个特点，就是前台传上来的文件会以 MultipartFile接口进行处理
	 * 而CommonsMultipartFile实现类进行实现
	 * 2.但是CommonsMultipartFile这个工具类虽然好用
	 * 但是有个缺点：目前没有好的方法将CommonsMultipartFile实现类进行初始化，
	 * 而只能通过前台文件上传控件将他的文件流传送过来才能进行初始化
	 * 
	 * 所以需要将CommonsMultipartFile转iFile
	 */

	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile)
	{
		File newFile=new File(cFile.getOriginalFilename());

		try {
			//将CommonsMultipartFile转iFile
			cFile.transferTo(newFile);

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block

			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			logger.error(e.toString());
			e.printStackTrace();
		}

		return newFile;
	}

	/**
	 * 设定项目的classpath路径也就是得到资源的绝对路径(当前项目的编译路径)
	 * @return classpath路径
	 * @throws Exception
	 */

	public static String getbasePath() throws Exception
	{
		//设定项目的classpath路径也就是得到资源的绝对路径(当前项目的编译路径)
		/*
		 * 当前项目的编译路径
		 * E:\自学网\java\project_1.Campus e-commerce project\o2o\target\classes
		 */

		//获取资源文件必须采用这种绝对路径的方法不要采用相对路径
		/*
		 * URI和URL的区别
		 * URI统一资源标识符，是针对整个资源的一个属性的管理对象，包括了URL
		 * URL统一资源定位符，是对资源的管理，如获取资源文件流等
		 * 查看了 URL和URI 对于getPath()方法的源代码，
		 * 前者URL是没有对转义字符的解码的过程
		 * 后者URL则是有一个解码的过程 java.net.URLDecoder.decode；
		 */
		//因为我的路径中是包含中文和空格符所以需要进行转义解码，故需要在绝对路径上加上URI，URI是需要解码的
		String basePath=Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		/*
		 * 因为我的路径含有中英文和空格所以需要属于URI解码过程将路径转换为UTF-8格式，否则会出现乱码问题
		 * 记住：只有在绝对路径上加上.toURI()才能进行解码
		 */
		java.net.URLDecoder.decode(basePath,"UTF8");	
		//返回classpath路径
		return  basePath;
	}

	/**
	 * 处理缩略图，并返回新生成图片的相对路径
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */


	/*
	 * CommonsMultipartFile是springweb自带的图片处理工具类
	 * tagetAddr:图片存储路径
	 */
	public static String generateThumbnail( ImgHolder thumbnail,String targetAddr){


		//因为传过来的图片名称可能出现重复，所以我们采用随机数对传进来的图片进行命名
		//根据getRandomFileName函数获得新生成的图片名称
		String realFileName=getRandomFileName();

		//获取传递过来的图片扩展名(如：jpg,png图片格式)
		String extension=getFileExtension(thumbnail.getImageName());

		/*
		 * 如果传入图片的地址中没有相对应的文件夹则需要创建
		 */
		makeDirPath(targetAddr);

		//图片存储路径+新生成的图片名称+图片格式生成相对路径
		String relativeAddr=targetAddr+realFileName+extension;

		//在日志中输入相对路径信息
		logger.debug("current relativeAddr is:"+relativeAddr);

		//采用图片的绝对路径，将图片传进文件流当中
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		//在日志中输入绝对路径信息
		logger.debug("current complete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
		try{

			//Thumbnails是图片处理工具，file文件中是正在进行处理的图片位置
			/*
			 * 图片采用绝对路径的原因：
			 * 图片最好不要放在项目下，指定一个绝对路径或者放置在另外一个服务器当中
			 * 否则当项目移动时，已经处理好的图片会消失
			 */
			Thumbnails.of(thumbnail.getImage())
			/*
			 * 1.重新设定图片的大小
			 * 2.在图片的左下角生成一个水印图片，水印图片位置在项目classpath路径下。以文件流的形式读取水印图片
			 * 3.设定水印图片的透明度
			 */
			.size(200, 200).watermark(Positions.BOTTOM_LEFT, ImageIO.read(new File(getbasePath()+"/watermark.jpg")), 0.25f)
			//1.将已经生成水印的图片压缩成80%
			//2.将处理好的缩略图输出在指定的地方
			.outputQuality(0.8f).toFile(dest);
		}catch(Exception e)
		{
			//如果路径生成失败，则输出错误信息
			logger.error(e.toString());

			e.printStackTrace();
		}
		//因为数据库表中有个字段需要获取图片路径，所以需要返回图片相对路径
		return relativeAddr;

	}
	
	/**
	 * 处理详情图，并返回新生成图片的相对值路径
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImgHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名如png,jpg等
		String extension = getFileExtension(thumbnail.getImageName());
		// 如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		// 获取文件存储的相对路径(带文件名)
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is :" + relativeAddr);
		// 获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		// 调用Thumbnails生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_LEFT, ImageIO.read(new File(getbasePath()+ "/watermark.jpg")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException("创建缩图片失败：" + e.toString());
		}
		// 返回图片相对路径地址
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及到的目录，即/home/work/xiangze/xxx.jpg
	 * 那么home,work,xiangze这三个文件夹自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {

		//获取图片路径
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;

		//将图片路径传到文件流当中
		File dirPath=new File(realFileParentPath);

		//如果文件流中的文件不存在，则自动创建文件夹
		if(!dirPath.exists()){
			dirPath.mkdirs();
		}

	}

	/**
	 * 获取图片文件的扩展名(文件格式，如：jpg,png等)
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {

	
		//截取文件名的最后一个.符号后面的剩余字符串，如xiaohuangren.jpg，即获取.符号后面的jpg三个字符
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前年月日时分秒+五位随机数
	 * @return
	 */
	public static String getRandomFileName() {
		//获取随机的五位数，随机数在10000-99999之间
		int rannum=r.nextInt(89999)+10000;

		//生成当前日期格式
		String nowTimeStr=sDateFormat.format(new Date());

		return nowTimeStr+rannum;
	}
	
	/**
	 * 当用户提交新的商铺图片的时候，我们需要先将原来的图片进行删除
	 * 判断storePath是文件的路径还是目录的路径 
	 * 如果storePath是文件路径则删除该文件
	 * 如果storePath是目录路径则删除该目录下的所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath){
		/**
		 * PathUtil.getImgBasePath()：获取图片的绝对路径E:/image/linyunying/image
		 * storePath：图片的相对路径
		 * 两者相加得到图片的全路径
		 */
		File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
		
		/**
		 * 如果文件存在则判断是文件路径还是目录路径
		 */
		if(fileOrPath.exists()){
			
			//1.判断是否是目录
			if(fileOrPath.isDirectory()){
				//获取所有目录
				File files[] =fileOrPath.listFiles();
				//将该目录下的所有文件逐一删除
				for(int i=0;i<files.length;i++){
					files[i].delete();
				}
			}
			//如果是文件则直接进行删除文件
			fileOrPath.delete();
		}
	}
	

	/**
	 * main函数。测试方法，为图片打上水印
	 * 为图片生成水印
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {


		//Thumbnails是图片处理工具，file文件中是正在进行处理的图片位置
		/*
		 * 图片采用绝对路径的原因：
		 * 图片最好不要放在项目下，指定一个绝对路径或者放置在另外一个服务器当中
		 * 否则当项目移动时，已经处理好的图片会消失
		 */
		Thumbnails.of(new File("E:/image/xiaohuangren.jpg"))
		/*
		 * 1.重新设定图片的大小
		 * 2.在图片的左下角生成一个水印图片，水印图片位置在项目classpath路径下。以文件流的形式读取水印图片
		 * 3.设定水印图片的透明度
		 */
		.size(200, 200).watermark(Positions.BOTTOM_LEFT, ImageIO.read(new File(getbasePath()+"/watermark.jpg")), 0.25f)
		//1.将已经生成水印的图片压缩成80%
		//2.将处理好的缩略图输出在指定的地方
		.outputQuality(0.8f).toFile("E:/image/xiaohuangrennew.jpg");

	}
}
