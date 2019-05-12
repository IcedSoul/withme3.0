let addFriendIndex = null;

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

function addThisUser(userId) {
    var text = document.getElementById('addFriendMessage').value;
    var usersId = new Array();
    usersId[0] = userId;
    sendMessage(text, usersId, 5);
    layer.close(addFriendIndex);
}