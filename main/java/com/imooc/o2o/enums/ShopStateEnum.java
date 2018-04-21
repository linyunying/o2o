package com.imooc.o2o.enums;

/**
 * 枚举类：
 * 1.枚举类其实就是相当于自给自足
 * 将构造方法设为私有的，自己类内部创建枚举对象，并且为成员变量赋值。最后将对象设置静态常量
 * 等我们需要的时候通过    类名.对象名.get方法     将需要的成员属性值获取到
 * 总结：简单来说就是将常规的静态常量进行封装使用
 * 
 * 
 */

public enum ShopStateEnum {
	
	/*
	 * 这个类似 public static final ShopStateEnum CHECK=new ShopStateEnum(0,审核中)
	 * 所以构造函数必须也传入对应的参数类，但是因为构造函数必须是私有的增加可读性
	 * 所以只能在自己类内部使用，创建对象。不能在外部创建枚举类对象
	 */
	//因为是封装的，所以需要通过int和String类型成员变量的set和get方法获取枚举值
	
	CHECK(0,"审核中"),OFFlINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),PASS(2,"通过认证"),
	INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"ShopId为空"),NULL_SHOP(-1003,"商铺信息为空");
	
	//想要获取上面的枚举值必须通过这两种类型的set和get方法获取
	private int state;
	
	private String  stateInfo;

	/*
	 * 私有构造函数，  为了不让枚举类被创建对象，
	 * 所以设置私有构造函数，并传入与上面对应的两个类型参数
	 */
	private ShopStateEnum(int state,String stateInfo ){
		
		this.state=state;
		
		this.stateInfo=stateInfo;
	}

	/**
	 * 普通方法
	 * 依据传入的state返回相应的枚举值
	 * @return
	 */
	public static ShopStateEnum stateOf(int state){
		/*
		 * 枚举的for循环，获取的是枚举常量，
		 * 即获取的是CHECK，OFFlINE...
		 */
		for(ShopStateEnum stateEnum:ShopStateEnum.values()){
			/*
			 * stateEnum.getState()==stateEnum.CHECK.getState()
			 * 获取到的是CHECK的state值0
			 * 如果传进来的状态值与枚举中的状态值一致，则返回对应的枚举常量名
			 * 其他类可根据这个方法获取枚举常量名从而获得对应的枚举值
			 */
			
			if(stateEnum.getState()==state){
				return stateEnum;
			}
		}
		//如果不满足要求则返回空值
		return null;
	}
	/*
	 * 删除set方法的原因：
	 * 因为枚举类就是常量定义，
	 * 所以不希望定义好的成员变量属性值通过set方法被改变，
	 * 跟构造函数被设为私有是一样的道理，
	 * 不希望在外部创建枚举类对象被改变成员变量值
	 */
	public int getState() {
		return state;
	}

	public String  getStateInfo() {
		return stateInfo;
	}
}
