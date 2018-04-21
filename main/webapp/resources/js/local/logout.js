/**
 * 点击退出系统按钮
 */
$(function(){
	
	$('#log-out').click(function(){
		//清除session
		$.ajax({
			url:"/o2o/local/logout",
			type:"post",
			async:false,
			cache:false,
			dataType:'json',
			success:function(data){
				if(data.success){
					var userType=$('#log-out').attr('usertype');
					//清除成功后退出到登录界面
					window.location.href="/o2o/local/login?usertype="+userType;
					return false;
				}
			},
			error:function(data,error){
				alert(error);
			}
		});
		
	});
});