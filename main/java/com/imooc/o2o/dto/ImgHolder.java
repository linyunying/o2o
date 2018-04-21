package com.imooc.o2o.dto;

import java.io.InputStream;

/**
 * 将前端传进来的图片以及图片名称放进该类中
 * @author Administrator
 *
 */
public class ImgHolder {
	
	
	private InputStream image;
	
	private String imageName;
	


	public ImgHolder(InputStream image,String imageName ) {
		
		this.imageName = imageName;
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
	
	

}
