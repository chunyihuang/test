/**
 * Created by pc on 2017/4/18.
 */
//退出逻辑
$(".logout").click(function () {
    $.ajax({
        type : "get",
        url : "/manage/loginOut",
        success : function(result) {
            if(result.status){
                delCookie("token");
                window.location.href = "login.html";
            }
        }
    });
});
