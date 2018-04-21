/**
 * 以下分成两个功能
 * 1.从后台获取到店铺分类信息以及区域分类填充至前端
 * 2.获取前台表单信息，转发至后台注册商铺信息
 * 
 * 
 */


 //jQuery语法
$(function(){
	// 从URL里获取shopId参数的值
	var shopId=getQueryString('shopId');
	// 由于店铺注册和编辑使用的是同一个页面，
	// 该标识符用来标明本次是注册店铺信息还是修改店铺信息
	var isEdit=shopId?true:false;
	
	//从后台获取店铺分类和区域分类
	var initUrl='/o2o/shopadmin/getshopinitinfo';

	//获取店铺注册controller层地址
	//注册店铺的URL
	var registeraShopUrl='/o2o/shopadmin/registerShop';
	
	// 编辑店铺前需要获取店铺信息，这里为获取当前店铺信息的URL
	var shopInfoUrl='/o2o/shopadmin/getshopbyid?shopId='+shopId;
	// 编辑店铺信息的URL
	var editShopUrl='/o2o/shopadmin/modifyshop';
	
	//调用函数。之所以放在前面是因为我们必须让页面初始化的时候就要有下拉框中的选项可供用户选择
	// 判断是编辑操作还是注册操作
	if (!isEdit) {
		getShopInitInfo();
	} else {
		getShopInfo(shopId);
	}
	
	
	
	/*
	 * 1.从后台获取店铺信息
	 *将获取到的信息赋给前端的店铺类别下拉框当中
	 */
	function getShopInfo(shopId){
		//将获取JSON格式中的data数据
		/*
		 * initUrl:通过访问controller层的/o2o/shopadmin/getshopinitinfo路径下的getShopInitInfo()方法
		 * 该方法会返回一个以JSON格式的map集合(集合中含有从数据库当中获取的商铺类别信息和区域信息)
		 */
		/*
		 * 前端通过getJSON()获取服务器中的数据，并对获取的数据进行解析，显示在页面中
		 */
		$.getJSON(shopInfoUrl,function(data){
		
			//如果服务器端controller层返回的success是true
			if(data.success){
			
				// 若访问成功，则依据后台传递过来的店铺信息为表单元素赋值
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				// 给店铺类别选定原先的店铺类别值
				var shopCategory = '<option data-id="'
						+ shop.shopCategory.shopCategoryId + '" selected>'
						+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml = '';
				// 初始化区域列表
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				// 不允许选择店铺类别
				$('#shop-category').attr('disabled', 'disabled');
				$('#area').html(tempAreaHtml);
				// 给店铺选定原先的所属的区域
				$("#area option[data-id='" + shop.area.areaId + "']").attr(
						"selected", "selected");
			}
		});
	}
	
	/*
	 * 1.从后台获取店铺分类ShopCategory信息和区域Area信息
	 *将获取到的信息赋给前端的店铺类别下拉框当中
	 */
	function getShopInitInfo(){
		//将获取JSON格式中的data数据
		/*
		 * initUrl:通过访问controller层的/o2o/shopadmin/getshopinitinfo路径下的getShopInitInfo()方法
		 * 该方法会返回一个以JSON格式的map集合(集合中含有从数据库当中获取的商铺类别信息和区域信息)
		 */
		/*
		 * 前端通过getJSON()获取服务器中的数据，并对获取的数据进行解析，显示在页面中
		 */
		$.getJSON(initUrl,function(data){
		
			//如果服务器端controller层返回的success是true
			if(data.success){
				var tempHtml='';
				var tempAreaHtml='';
				
				/*
				 * 取出map集合中的shopCategoryList集合进行遍历
				 */
				//对后台的商铺类别列表进行遍历，item=data.shopCategoryList,index是遍历数组中的索引
				data.shopCategoryList.map(function(item,index){
					/*
					 * 将类别信息遍历成字符串
					 * <option data-id="1">奶茶</option>
					 * ...
					 * <option data-id="n">烧烤</option>
					 */
					tempHtml +='<option data-id="'+item.shopCategoryId+'">'
					+ item.shopCategoryName+'</option>';
				});
				
				//区域类别
					data.areaList.map(function(item,index){
						tempAreaHtml+='<option data-id="'+item.areaId+'">'
					+item.areaName+'</option>';
					});
					/*
					 * 将上面编辑好的<option>元素添加进下拉框当中以供用户选择
					 */
					$('#shop-category').html(tempHtml);
					$('#area').html(tempAreaHtml);
				
			}
		});
	}
		/*
		 * 2.从前台获取表单信息通过ajax发送到后台当中
		 */
		//点击提交获取到表单内容
		$('#submit').click(function(){
			//shop是一个JSON对象
			
			var shop={};
			// 若属于编辑，则给shopId赋值
			if(isEdit){
				shop.shopId=shopId;
			}
			//获取到前端用户输入的店铺信息
			shop.shopName=$('#shop-name').val();
			shop.shopAddr=$('#shop-addr').val();
			shop.phone=$('#shop-phone').val();
			shop.shopDesc=$('#shop-desc').val();
		
			/*
			 * 获取下拉框中的商铺类别id
			 */
			shop.shopCategory={
					shopCategoryId:$('#shop-category').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			/*
			 * 获取下拉框中区域id
			 */
			shop.area={
					areaId:$('#area').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			//获取用户上传的文件流
			var shopImg=$('#shop-img')[0].files[0];
			
			//将前台获取到的表单信息和照片文件保存到FormData对象当中
			var formData=new FormData();
			formData.append('shopImg',shopImg);
			//将获取的信息以JSON格式的字符串进行保存
			formData.append('shopStr',JSON.stringify(shop));
			
			/*
			 * 验证码提交
			 */
			//当我们点击提交按钮的时候，将用户输入的验证码信息取出传到后台，验证是否有误
			var verifyCodeActual=$('#j_kaptcha').val();
			//如果验证码内容为空，则提醒用户输入验证码，并返回一个验证码图片
			if(!verifyCodeActual){
				$.toast('请输入验证码！');
				//返回验证码
				return;
			}
			//如果不为空则将用户填写的验证码添加到formData对象，将信息传递给后台
			formData.append("verifyCodeActual",verifyCodeActual);
			//通过ajax将信息传送到后台当中
			
			
			
			
			$.ajax({
			
				url:(isEdit?editShopUrl:registeraShopUrl) ,
				//请求方法POST
				type:'POST',
				//返回数据
				data:formData,
				
				contentType:false,
				
				processData:false,
				
				cache:false,
				//data是后台返回的JSON数据
				success:function(data){
					//如果后台接收之后返回的success是true则证明验证码提交成功
					if(data.success){
						$.toast('提交成功！');
					}else{
						$.toast('提交失败！'+data.errMsg);
					}
					
					//我们输入验证码点击提交之后需要再次更换新的验证码图片
					$('#kaptcha_img').click();
				} 
			});
			
			});
})