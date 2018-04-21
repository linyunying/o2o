/**
 * 分页查询店铺
 */
$(function(){

	getlist();
	
	function getlist(e){
		
		/**
		 * 通过ajax获取后端的店铺信息列表数据和用户信息
		 */
		$.ajax({
			url:"/o2o/shopadmin/getshoplist",
			type:'get',
			dataType:'json',
			success:function(data){
				/*如果后端传递的参数success为true
				 * 则将商铺列表信息和用户信息作为参数分别传入不同的函数进行执行
				 * 
				 */
				if(data.success){
					//在前端列出商铺信息列表信息的函数
					handleList(data.shopList);
					//在前端显示出用户信息函数
					handleUser(data.user);
				}
			}
		});
	}
	//在前端指定位置显示用户名称
	function handleUser(data){
		$('#user-name').text(data.name);		
	}
	//将店铺信息列表在前端显示
	function handleList(data){
		var html='';
		data.map(function(item,index){
			html+='<div class="row row-shop"><div class="col-40">'
			+item.shopName+'</div><div class="col-40">'
			+shopStatus(item.enableStatus)
			+'</div> <div class="col-20">'
			+goShop(item.enableStatus,item.shopId)+'</div></div>';
		});
		$('.shop-wrap').html(html);
	}
	//根据后端传递过来的店铺状态值返回相应的字符
	function shopStatus(status){
		if(status==0){
			return '审核中';
		}else if(status==-1){
			return '店铺非法';
		}else if(status==1){
			return '审核通过';
		}
	}
	
	
	function goShop(status,id){
		//只有店铺审核通过才能进行店铺管理页面
		if(status==1){
			return '<a href="/o2o/shopadmin/shopmanagement?shopId='+id+'">进入</a>';
		}else{
			return '';
			}
	}
});