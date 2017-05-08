/*
 * login
 * 登录验证类
 */
var login = {
	_input: ["userName","password"],

	inputVerify: function(i, m){
		if(arguments.length === 1){
			m = "用户名或密码不能为空";
		}
		var input = $("#" + i).val();
		if(input === ""){
			$("#errorInfo").text(m);
			$("#errorInfo").css("opacity",1);
			return false;
		}else{
			$("#errorInfo").text("&nbsp;");
			$("#errorInfo").css("opacity",0);
			return true;
		}
	},
	submitVerify: function(){
		this._input.forEach(function(v, i, a) {
			var b = login.inputVerify(v);
			if(b){
				if(i === a.length-1){
					var u = $.trim($("#userName").val());
					var p = $("#password").val();
					login.doLogin(u, p);
					return true;
				}
			}else{
				return false;
			}
		});
	},
	doLogin: function(u, p){
        var loading = layer.load(0, {shade: false});
		$.ajax({
			url: "/player/login",
			type: "get",
			data: {
				userName: u,
				password: p
			},
			async: false,
			success: function(res){
                layer.close(loading);
                if(res.status){
                    setCookie("userToken",res.token);
                    setCookie("userName",res.detail.user);
                    setCookie("userId",res.detail.id);
                    location.href = "index.html";
                }else{
                    layer.close(loading);
                    $("#errorInfo").text(res.reason);
                    $("#errorInfo").css("opacity",1);
				}
				return false;
			},
			error: function(err){
				alert("错误：" + JSON.stringify(err));
				return false;
			}

		});
	}
}