function checkRegister() {
    var user = {};
    user.userName = document.getElementById("userName").value;
    user.userNickName = document.getElementById("userNickName").value;
    user.userPassword = document.getElementById("userPassword").value;
    if (user.userName == '') {
        layer.msg('用户名不能为空', {icon: 2});
        return;
    }
    else if (user.userName.length >= 12) {
        layer.msg('用户名长度不能超过12个字符', {icon: 2});
        return;
    }
    if (user.userNickName == '') {
        layer.msg('昵称不能为空', {icon: 2});
        return;
    }
    else if (user.userNickName.length >= 15) {
        layer.msg('用户名长度不能超过15个字符', {icon: 2});
        return;
    }
    else if (user.userPassword == '') {
        layer.msg('密码不能为空', {icon: 2});
        return;
    }
    else if (user.userPassword.length >= 20) {
        layer.msg('密码长度不能超过20个字符', {icon: 2});
        return;
    }
    var jsonObj = {};
    jsonObj.jsonObj = JSON.stringify(user);
    var registerResult = null;
    $.ajax({
        async: false,
        type: 'POST',
        url: address + 'v1/user/register',
        data: jsonObj,
        dataType: 'json',
        success: function (result) {
            registerResult = result;
        },
        error: function () {
            layer.alert('网络错误');
        }
    });
    if (registerResult.status == 1) {
        layer.msg('注册成功', {icon: 1});
        window.location.href = "login.html";
    }
    else {
        layer.msg(registerResult.message, {icon: 2});
    }
}