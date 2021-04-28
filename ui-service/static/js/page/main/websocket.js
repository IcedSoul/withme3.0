//获取服务端地址

let ws = wsAddress + parseURL("token");
// let ws = "ws://10.141.211.176:21002/" + parseURL("token");
let websocket = null;

//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket(ws);
} else {
    layer.alert('对不起，你的浏览器不支持WebSocket', {
        icon: 2
    });
}


//连接成功建立的回调方法
websocket.onopen = function () {
    //显示在线状态
    //通知自己的所有好友自己上线啦
    onLineStatusNotice(3);
    showOfflineMessage();
};

//接收到消息的回调方法
websocket.onmessage = function (event) {
    //接收到消息，处理消息
    handleReceiveMessage(event.data);
};

//连接发生错误的回调方法
websocket.onerror = function () {
    layer.alert('连接异常，请见谅');
};

//连接关闭的回调方法
websocket.onclose = function () {
    //连接关闭，告诉所有好友我下线啦
    onLineStatusNotice(4);
    layer.alert('感谢您的使用，再见');
};

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
};

//关闭WebSocket连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function sendMessage(content, usersId, type) {
    websocket.send(JSON.stringify({
        from: getCurrentUser().userId,
        to: usersId,
        content: content,
        type: type,
        time: getDateFull()
    }));
}