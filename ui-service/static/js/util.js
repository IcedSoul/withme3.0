let address = "/";
let wsAddress = "ws://localhost:8282/";

function setCookie(name, value, time) {
    let exp = new Date();
    exp.setTime(exp.getTime() + time * 60 * 60 * 1000); //3天过期
    document.cookie = name + "=" + encodeURIComponent(value)
        + ";expires=" + exp.toGMTString() + ";path=/";
    return true;
};

function getCookie(name) {
    let arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function makeURL(url, name, value) {
    return encodeURI(url + "?" + name + "=" + value);
}

function parseURL(name) {
    let url = location.search;
    let request = new Object();
    if (url.indexOf("?") != -1) {
        let str = url.substr(1);
        let strs = str.split("&");
        for (let i = 0; i < strs.length; i++) {
            let value = strs[i].split("=");
            if (value[0] == name)
                return decodeURI(value[1]);
        }
    }
    return null;
}

function getCurrentUser() {
    let token = parseURL("token");
    let response = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/user/token/' + token,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function () {
            layer.alert('网络错误');
        }
    });
    if (response.status !== 1) {
        layer.msg("登录状态失效，请重新登录");
        window.location.href = "login.html";
    }
    else
        return JSON.parse(response.content);
}

function getUserById(id) {
    let user = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/user/id/' + id,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                user = JSON.parse(result.content);
            }
            else {
                layer.alert('查询错误');
            }
        },
        error: function (result) {
            layer.alert('查询错误');
        }
    });
    return user;
}