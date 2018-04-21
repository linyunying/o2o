/**
 * 
 */
$(function() {
	//获取网页中的shopId
	var shopId = getQueryString('shopId');
	//获取到URL中的shopId，根据URL中的shopId进入到店铺管理页面
	var shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId=' + shopId;
	
	//根据shopInfoUrl访问后端，如果该shopInfoUrl中带有shopId则直接从访问到商铺管理页面
	$.getJSON(shopInfoUrl, function(data) {
		//如果后台传递过来的redirect为true则说明无法进行到商铺管理页面，需要返回到之前的店铺列表页面
		if (data.redirect) {
			//进入到后端传递过来的店铺列表页面当中
			window.location.href = data.url;
		} else {
			//如果后端传来的redirect为false则说明已经可以进入商铺管理页面，我们可以获取到商铺管理页面的URL中的shopId对商铺信息进行编辑
			//如果后端传来的shopId有效则为shopId赋值，即可访问商铺管理页面
			if (data.shopId != undefined && data.shopId != null) {
				
				shopId = data.shopId;
			}
			//将后端获取到是ShopId赋值给商铺信息链接，直接通过商铺信息链接对商铺信息进行编辑
			$('#shopInfo')
					.attr('href', '/o2o/shopadmin/shopoperation?shopId=' + shopId);
		}
	});
});