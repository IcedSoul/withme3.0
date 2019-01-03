var address = "http://localhost:8080/WithMeAlpha/";

function setCookie(name, value ,time) {
    var exp = new Date();
    exp.setTime(exp.getTime() + time * 60 * 60 * 1000); //3天过期
    document.cookie = name + "=" + encodeURIComponent(value)
        + ";expires=" + exp.toGMTString() + ";path=/";
    return true;
};

function getCookie(name) {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function makeURL(url,name,value) {
    return encodeURI(url+"?"+name+"="+value);
}

function parseURL(name) {
    var url = location.search;
    var request = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        var strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            var value = strs[i].split("=");
            if( value[0] == name)
                return decodeURI(value[1]);
        }
    }
    return null;
}

function getCurrentUser() {
    var token = {};
    token.token = parseURL("token");
    var jsonObj = {};
    jsonObj.jsonObj = JSON.stringify(token);
    var response = null;
    $.ajax({
        async : false, //设置同步
        type : 'POST',
        url : address+'user/getCurrentUser',
        data : jsonObj,
        dataType : 'json',
        success : function(result) {
            response = result;
        },
        error : function() {
            layer.alert('网络错误');
        }
    });
    if(response.status == -1){
        layer.msg("登录状态失效，请重新登录");
        window.location.href="login.html";
    }
    else
        return JSON.parse(response.content);
}