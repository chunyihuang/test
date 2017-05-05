/**
 * Created by pc on 2017/4/18.
 */
//登录逻辑
$("#login").click(function () {
    $.ajax({
        type : "post",
        url : "/manage/login",
        data : {
            "userName":$("#username").val(),
            "password":$("#password").val()
        },
        success : function(result) {
            if ( result.status ) {
                setCookie("token",result.token);
                setCookie("adminName",result.detail.userName);
                window.location.href = "index.html";
            } else {
                $("#errorInfo").removeClass("errorInfo");
            }
        }
    });
});
