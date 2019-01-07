function checkLogin() {
    var user = {};
    user.userName = document.getElementById("userName").value;
    user.userPassword = document.getElementById("userPassword").value;
    if (user.userName == '') {
        layer.msg('用户名不能为空', {icon: 2});
        return;
    }
    else if (user.userName.length >= 12) {
        layer.msg('用户名长度不能超过12个字符', {icon: 2});
        return;
    }
    else if (user.userPassword == '') {
        layer.msg('密码不能为空', {icon: 2});
        return;
    }
    var jsonObj = {};
    jsonObj.jsonObj = JSON.stringify(user);
    var loginResult = null;
    $.ajax({
        async: false, //设置同步
        type: 'POST',
        url: address + 'user/login',
        data: jsonObj,
        dataType: 'json',
        success: function (result) {
            loginResult = result;
        },
        error: function () {
            layer.alert('网络错误');
        }
    });
    if (loginResult.status == 1) {
        layer.msg(loginResult.message, {icon: 1});
        window.location.href = makeURL("main.html", "token", loginResult.content);
    }
    else {
        layer.msg(loginResult.message, {icon: 2});
    }
}