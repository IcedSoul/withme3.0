let addFriendIndex = null;


/**
 * 弹出输入用户名窗口
 */
function searchFriend() {
    layer.prompt({
        title: '请输入用户名：',
        zIndex: 20000000
    }, function (userName, index) {
        layer.close(index);
        findUser(userName);
    });
}

/**
 * 查询用户并且弹出搜索结果框
 * @param userName
 */
function findUser(userName) {
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/user/userName/' + userName,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                let user = JSON.parse(result.content);
                addFriend(user.userId, user.userName, user.userNickName);
            } else {
                layer.msg('查找的人不存在', {
                    icon: 6,
                    zIndex: 20000001,
                    time: 2000
                });
            }
        },
        error: function (result) {
            layer.msg('查找失败', {
                icon: 6,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
}

/**
 * 打开搜索用户结果窗口
 * @param userId
 * @param userName
 * @param userNickName
 */
function addFriend(userId, userName, userNickName) {
    let html = '<div id="addFriendContent" class="addFriend">'
        + '<div class="container addFriendBox">'
        + '<div class="row addFriendRow">'
        + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 addFriendImgBox">'
        + '<img src="img/photo.jpg" class="img-circle addFriendImg"/>'
        + '</div>'
        + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
        + '用&nbsp;&nbsp;户&nbsp;&nbsp;名：'
        + userName
        + '</div>'
        + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
        + '昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：'
        + userNickName
        + '</div>'
        + '<div class="col-md-6 col-sm-6 col-xs-6 col-lg-6 addFriendInfo">'
        + '附加消息：'
        + '<textarea id="addFriendMessage" class="form-control addFriendTextArea" rows="2"></textarea>'
        + '</div>'
        + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
        + '<button class="btn btn-default" onclick=\'addThisUser(' + userId + ')\'>建立联系</button>'
        + '</div>' + '</div>' + '</div>' + '</div>';
    addFriendIndex = layer.open({
        type: 1,
        title: '查找结果',
        content: html,
        area: ['500px', '275px'],
        resize: false,
        id: 'addFriend',
        zIndex: 20000001
    });
}

/**
 * 添加用户为好友
 * @param userId
 */
function addThisUser(userId) {
    let text = document.getElementById('addFriendMessage').value;
    let usersId = new Array();
    usersId[0] = userId;
    sendMessage(text, usersId, 5);
    layer.close(addFriendIndex);
}

/**
 * 同意好友申请
 * @param userId
 */
function agreeAddThisUser(userId) {
    let text = '';
    let addUser = {};
    addUser.userIdA = userId;
    addUser.userIdB = currentUser.userId;
    $.ajax({
        async: false, //设置同步
        type: 'POST',
        url: address + 'v1/userRelations',
        data: addUser,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                text = '我已经同意了你的好友申请，快一起来搞点事情吧！';
            } else {
                text = '是上天不允许我们建立联系啊！';
            }
        },
        error: function (result) {
            layer.msg('添加失败', {
                icon: 6,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
    let usersId = [];
    usersId[0] = userId;
    sendMessage(text, usersId, -1);
    let addFriendRow = document.getElementById(userId + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber === 0)
        layer.close(relationApply);
    //刷新好友列表
    let relationList = document.getElementById('relationList');
    if (relationList) {
        layer.close(relationListIndex);
    }
}

/**
 * 拒绝好友申请
 * @param userId
 */
function refuseAddThisUser(userId) {

    //let text = '落花有意，流水无情。相见想闻，不如不见不闻。';
    let text = '系统抛了一枚硬币，觉得你俩不合适，所以驳回了你的申请。';
    let usersId = [];
    usersId[0] = userId;
    sendMessage(text, usersId, -1);
    let addFriendRow = document.getElementById(userId + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber === 0)
        layer.close(relationApply);
}