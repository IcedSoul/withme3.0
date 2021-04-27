//单人聊天窗口
function chatWithSomeBody(userId, userName, userNickName) {
    let chatWith = '<div class="chatWith">'
        + '<div id="' + userId + 'output" class="container output">'
        + '</div>'
        + '<hr/>'
        + '<div class="options">'
        + '<img class="option" src="img/face.png">'
        + '<img class="option" src="img/img.png">'
        + '<button type="button" class="btn btn-default pull-right" onclick="sendMessagePre('
        + userId
        + ')">发送</button>'
        + '</div>'
        + '<div class="input">'
        + '<textarea id="'
        + userId
        + 'messageText" class="form-control" onkeydown="binfEnter(event,'
        + userId + ')"></textarea>' + '</div>' + '</div>';
    layer.open({
        type: 1,
        title: [userNickName, 'font-family:Microsoft YaHei;font-size: 24px;height: 80px;'],
        content: chatWith,
        area: ['600px', '600px'],
        maxmin: true,
        offset: ['100px', '100px'],
        shade: 0,
        id: userName,
        resize: true,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });

    //获取消息记录
    let allMessages = getMessageRecordBetweenUsers(userId, 0, 30);
    for (let i = 0; i < allMessages.length; i++) {
        let usersId = new Array();
        usersId[0] = allMessages[i].toId;
        let jsonMessage = JSON.stringify({
            from: allMessages[i].fromId,
            to: usersId,
            content: allMessages[i].content,
            type: allMessages[i].type,
            time: allMessages[i].time
        });
        showReceiveMessage(allMessages[i].content, allMessages[i].fromId, usersId, allMessages[i].type, allMessages[i].time, jsonMessage);
    }
}

//监听输入框回车键
function binfEnter(obj, toUserId) {
    if (obj.keyCode == 13) {
        sendMessagePre(toUserId);
        obj.preventDefault();
    }
}

//发送消息传递准备工作
function sendMessagePre(toUserId) {
    let textAreaId = toUserId + 'messageText';
    let message = document.getElementById(textAreaId).value;
    if (message != '') {
        let toUsersId = new Array();
        toUsersId[0] = toUserId;
        sendMessage(message, toUsersId, 0);
        document.getElementById(textAreaId).value = '';
    }
}

//ajax获取两用户之间的消息记录
function getMessageRecordBetweenUsers(userId, page, number) {
    let allMessages = null;
    let twoUser = {};
    twoUser.userIdA = currentUser.userId;
    twoUser.userIdB = userId;
    twoUser.page = page;
    twoUser.number = number;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/messages/' + twoUser.userIdA + "/" + twoUser.userIdB + "/" + twoUser.page + "/" + twoUser.number,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                allMessages = result.content;
            }
            else {
                layer.alert('查询错误');
            }
        },
        error: function (result) {
            layer.alert('查询错误');
        }
    });
    //划重点划重点，这里的eval方法不同于prase方法，外面加括号
    allMessages = eval("(" + allMessages + ")");
    return allMessages;
}