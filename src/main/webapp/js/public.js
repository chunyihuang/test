		//添加退出登录事件,清空cookie返回首页
		$('#login-stauts').on('click','#exitlogin',function(){
			delCookie('userName');
			delCookie('userToken');
			delCookie('userId');
			console.log(document.cookie);
			window.location.href = '../index.html';
		})
		//检测是否登录	
		if(getCookie('userToken')!=""){
			//如果登录了
			$('#login-stauts').html('<a href="userInfo.html">'+getCookie('userName')+'</a><a class="ml10" id="exitlogin">退出</a>');
			$('#person').attr('href','userInfo.html');
		}else{
		}