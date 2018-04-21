package com.imooc.o2o.dto;

/**
 * 封装JSON对象，所有返回结果都使用它
 * @author Administrator
 *
 * @param <T>
 */
public class Result<T> {

	private boolean success;//是否成功标志

	private T data;//成功时返回的数据

	private String errorMsg;

	private int errorCode;

	//无参构造函数
	public Result(){

	}

	//成功时的构造器
	public Result(boolean success,T data){
		this.success=success;
		this.data=data;
	}
	
	//错误时的构造器
	public Result(boolean success,int errorCode, String errorMsg ){
		this.success=success;
		this.errorCode=errorCode;
		this.errorMsg=errorMsg;
	}
	

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
