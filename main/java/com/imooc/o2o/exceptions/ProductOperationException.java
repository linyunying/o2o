package com.imooc.o2o.exceptions;

/**
 * 继承RuntimeException异常，当抛出该异常的时候可以支持事物回滚
 * 即如果在提交表单时有一个地方出错数据库就不会接收表单信息
 * 简单来说就是表单信息要么全部正确才能一起提交，要么就不提交
 * @author Administrator
 *
 */
public class ProductOperationException extends RuntimeException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3882011364906825791L;

	/**
	 * 返回自定义异常
	 * @param msg
	 */
	public ProductOperationException(String msg){
		super(msg);
	}

}
