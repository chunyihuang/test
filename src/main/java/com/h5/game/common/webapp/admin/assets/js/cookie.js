/**
 * Created by pc on 2017/4/18.
 */
//种植cookiie
function setCookie(name, value) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 1 * 60 * 60 * 1000);//有效期1小时
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
//读取cookie
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');

    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1);
        if (c.indexOf(name) != -1) {
            return unescape(c.substring(name.length, c.length));
        }
    }
    return "";
}
function  delCookie(name) {
    var self = this;
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=self.getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}