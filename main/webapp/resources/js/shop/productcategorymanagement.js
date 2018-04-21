$(function() {
	//获取商品信息列表
	var listUrl = '/o2o/shopadmin/getproductcategorylist';
	//添加商品类别信息
	var addUrl = '/o2o/shopadmin/addproductcategorys';
	//删除商品类别信息
	var deleteUrl = '/o2o/shopadmin/removeproductcategory';
	getList();
	function getList() {
		$
				.getJSON(
						listUrl,//访问商品信息列表URL获取商品信息类别信息
						function(data) {//获取后端返回的商品类别列表
							if (data.success) {
								var dataList = data.data;//将商品类别信息加载，循环遍历出在前端
								$('.category-wrap').html('');
								var tempHtml = '';
								dataList//循环遍历出商品类别信息
										.map(function(item, index) {
											//之前已经存在的表单信息是以now进行标识
											tempHtml += ''
													+ '<div class="row row-product-category now">'
													+ '<div class="col-33 product-category-name">'
													+ item.productCategoryName
													+ '</div>'
													+ '<div class="col-33">'
													+ item.priority
													+ '</div>'
													+ '<div class="col-33"><a href="#" class="button delete" data-id="'
													+ item.productCategoryId
													+ '">删除</a></div>'
													+ '</div>';
										});
								$('.category-wrap').append(tempHtml);
							}
						});
	}
	//添加商品类别信息，每次点击一次新增按钮，就添加一列空的文本以供用户进行添加信息
	$('#new')
			.click(
					function() {
						var tempHtml = '<div class="row row-product-category temp">'
								+ '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div>'
								+ '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
								+ '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
								+ '</div>';
						$('.category-wrap').append(tempHtml);
					});
	//点击提交，获取用户新增加的表单信息，之前的不要添加进数据库，就是只提交.temp表单中的信息
	$('#submit').click(function() {
		var tempArr = $('.temp');
		var productCategoryList = [];
		//将表单中的一行信息进行遍历添加
		tempArr.map(function(index, item) {
			var tempObj = {};//每行的商品类别信息集合
			tempObj.productCategoryName = $(item).find('.category').val();
			tempObj.priority = $(item).find('.priority').val();
			if (tempObj.productCategoryName && tempObj.priority) {
				//将每行的商品类别信息添加进集合当中
				productCategoryList.push(tempObj);
			}
		});
		//将前端信息通过ajax提交到后台
		$.ajax({
			url : addUrl,
			type : 'POST',
			data : JSON.stringify(productCategoryList),
			contentType : 'application/json',
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					getList();
				} else {
					$.toast('提交失败！');
				}
			}
		});
	});

	//点击删除按钮，将商品类别信息删除
	$('.category-wrap').on('click', '.row-product-category.temp .delete',
			function(e) {
				console.log($(this).parent().parent());
				$(this).parent().parent().remove();

			});
	$('.category-wrap').on('click', '.row-product-category.now .delete',
			function(e) {
				var target = e.currentTarget;
				$.confirm('确定么?', function() {
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {
							productCategoryId : target.dataset.id
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.toast('删除成功！');
								getList();
							} else {
								$.toast('删除失败！');
							}
						}
					});
				});
			});
});