function showOfflineMessage() {
    let allMessages = getOfflineMessage()
    for (let i = 0; i < allMessages.length; i++) {
        let usersId = [];
        usersId[0] = allMessages[i].toId;
        let jsonMessage = JSON.stringify({
            from: allMessages[i].fromId,
            to: usersId,
            content: allMessages[i].content,
            type: allMessages[i].type,
            time: allMessages[i].time
        });
        handleReceiveMessage(jsonMessage);
        showReceiveMessage(allMessages[i].content, allMessages[i].fromId, usersId, allMessages[i].type, allMessages[i].time, jsonMessage);
    }
}

function getOfflineMessage() {
    let allMessages = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/offlineMessages/toId/' + currentUser.userId,
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
            layer.alert('网络异常');
        }
    });
    allMessages = eval("(" + allMessages + ")");
    return allMessages;
}