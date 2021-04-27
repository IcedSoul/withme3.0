

// "message"{
//			"from": "xxxx",
//			"to":["xxx"],
//			"content":"xxxxx",
//			"type":0,
//			"time":"xxxx.xx.xx xx.xx.xx"
//		}

let noticeIndex = null;
let noticeUser = new Array();
let noticeMessage = new Array();
let noticeCount = 0;
let statusChangeMark = 0;

let currentUser = getCurrentUser();

//处理接收到的数据
function handleReceiveMessage(message) {
    messages = JSON.parse(message);
    //判断是否为上下线通知
    if (messages.type == 3 || messages.type == 4) {
        changeToOnlineStatus(messages.content, messages.from, messages.to, messages.type, messages.time, message);
    }
    else if (messages.type == 5 || messages.type == 6) {
        openRelationApply(messages.content, messages.from, messages.to, messages.type, messages.time, message);
    }
    else if (messages.type == 0 || messages.type == -1 || messages.type == 1)
        showReceiveMessage(messages.content, messages.from, messages.to, messages.type, messages.time, message);
}



//将消息显示在网页上
function showReceiveMessage(content, from, to, type, time, message) {
    if(type === 3 || type === 4){
        return;
    }
    let times = time.split(' ');
    let now = getDateFull();
    let nows = now.split(' ');
    let showTime = times[1];
    if (nows[0] != times[0]) {
        showTime = time;
    }
    if (from == currentUser.userId) {
        let messageReceiver = '#' + to[0] + 'output';
        let target = document.getElementById(to[0] + 'output');
        if (type == 1) {
            messageReceiver = '#' + to[0] + 'outputGroup';
            target = document.getElementById(to[0] + 'outputGroup');
        }
        let rightArrow = '<div class="row singleMessage">'
            + '<div class="col-md-11 col-sm-11 col-xs-11 col-lg-11 text">'
            + '<div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 timePositionRight">'
            + '<div id="time" class="timeRight">'
            + showTime
            + '</div>'
            + '</div>'
            + '<div class="arrowBoxRight">'
            + '<div class="message">'
            + content
            + '</div>'
            + '</div>'
            + '</div>'
            + '<div id="icons" class="col-md-1 col-sm-1 col-xs-1 col-lg-1  iconsRight">'
            + '<img class="img-circle iconssRight" src="img/photo.jpg">'
            + '</div>' + '</div>';

        if (target) {
            $(messageReceiver).append(rightArrow);
            target.scrollTop = target.scrollHeight;
        }
        else {
            doMessageNotice(content, from, to, type, time, message);
        }
    } else {
        let messageReceiver = '#' + from + 'output';
        let target = document.getElementById(from + 'output');
        if (type === 1) {
            messageReceiver = '#' + to[0] + 'outputGroup';
            target = document.getElementById(to[0] + 'outputGroup');
            let fromUser = getUserById(from);

            showTime = fromUser.userNickName + '&nbsp;&nbsp;' + showTime;
        }
        leftArrow = '<div class="row singleMessage">'
            + '<div id="icons" class="col-md-1 col-sm-1 col-xs-1 col-lg-1  iconsLeft">'
            + '<img class="img-circle iconssLeft" src="img/photo.jpg">'
            + '</div>'
            + '<div class="col-md-11 col-sm-11 col-xs-11 col-lg-11 text">'
            + '<div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 timePositionLeft">'
            + '<div class="timeLeft">'
            + showTime
            + '</div>'
            + '</div>' + '<div class="arrowBoxLeft">'
            + '<div id="leftMessage" class="message">' + content
            + '</div>' + '</div>' + '</div>' + '</div>';
        if (target) {
            $(messageReceiver).append(leftArrow);
            target.scrollTop = target.scrollHeight;
        }
        else
            doMessageNotice(content, from, to, type, time, message);
    }
}


//消息通知
function doMessageNotice(content, from, to, type, time, message) {
    let fromUser = null;
    if(type !== 7){
        fromUser = getUserById(from);
    }
    if (noticeIndex == null) {
        let html = '<div class="notice">' +
            '<div class="noticePosition" onclick="openNotice();">' +
            '<marquee id="noticeText">' +
            '</marquee>' +
            '</div>' +
            '</div>';
        noticeIndex = layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            content: html,
            shade: 0,
            offset: 't',
            area: ['180px', '40px'],
            anim: 6,
            id: 'notice'
        });
    }
    if (type === 0 || type === -1 || type === 1 || type === 7) {
        noticeUser[noticeCount] = fromUser;
        noticeMessage[noticeCount] = message;
        noticeCount++;
    }
    let noticeText = document.getElementById('noticeText');
    let text = '';
    if (type === 0 || type === -1) {
        text += fromUser.userNickName + ':' + content;
    }
    else if (type === 3) {
        text = '您的联系人&nbsp;' + fromUser.userNickName + '&nbsp;上线了';
        statusChangeMark = 1;
    }
    else if (type === 4) {
        text = '您的联系人 &nbsp;' + fromUser.userNickName + '&nbsp;下线了';
        statusChangeMark = 1;
    }
    else if (type === 1) {
        let group = ajaxGetGroupById(to[0]);
        text = group.groupName + '|' + fromUser.userNickName + ':' + content;
    }
    else if (type === 7) {
        let group = ajaxGetGroupById(from);
        text = group.groupName + ' 有 ' + content + " 条新消息";
    }
    text += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
    noticeText.innerHTML += text;
}

function openNotice() {
    layer.close(noticeIndex);
    if (statusChangeMark === 1) {
        listRelation();
    }
    for (let i = 0; i < noticeCount; i++) {
        let messages = JSON.parse(noticeMessage[i]);
        if (messages.type === 0 || messages.type === -1) {
            chatWithSomeBody(noticeUser[i].userId, noticeUser[i].userName, noticeUser[i].userNickName);
        }
        else if (messages.type === 1) {
            let group = ajaxGetGroupById(messages.to[0]);
            chatWithGroup(group.id, group.groupId, group.groupName, group.groupCreatorId);
        }
        else if (messages.type === 7) {
            let group = ajaxGetGroupById(messages.from);
            chatWithGroup(group.id, group.groupId, group.groupName, group.groupCreatorId);
        }
    }
    noticeIndex = null;
    noticeUser.splice(0, noticeUser.length);
    noticeMessage.splice(0, noticeMessage.length);
    noticeCount = 0;
    statusChangeMark = 0;
}

function appendZero(s) {
    return ("00" + s).substr((s + "").length);
} //补0函数

//获取当前时间日期
function getDateFull() {
    let date = new Date();
    let currentdate = date.getFullYear() + "-"
        + appendZero(date.getMonth() + 1) + "-"
        + appendZero(date.getDate()) + " "
        + appendZero(date.getHours()) + ":"
        + appendZero(date.getMinutes()) + ":"
        + appendZero(date.getSeconds());
    return currentdate;
}

function closeWindow() {
    window.opener = null;
    window.open('', '_self');
    window.close();
}

layer.ready(function () {
    listRelation();
});

//使用ajax获取当前用户的所有好友，现在大概知道json转来转去的过程了，上面两处使用ajax不太规范，但是改着太麻烦就不改了，能用就成
function getAllRelations() {
    let response = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/users/friends/' + currentUser.userId,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.alert('查询错误');
        }
    });
    if (response.status === -1) {
        layer.alert('查询错误');
        return null;
    }
    else {
        if(response.content != "") {
            return eval("(" + response.content + ")");
        }
        else
            return [];
    }
}

function onLineStatusNotice(type) {
    let allRelations = getAllRelations();
    let content = null;
    if (type === 3)
        content = '上线通知';
    else if (type === 4)
        content = '下线通知';
    let usersId = [];
    for (let i = 0; i < allRelations.length; i++) {
        usersId[i] = allRelations[i].userId;
    }
    sendMessage(content, usersId, type);
}


function changeToOnlineStatus(content, from, to, type, time, message) {
    let onLineStatus = document.getElementById(from + 'onlineStatus');
    let offLineStatus = document.getElementById(from + 'offlineStatus');
    //判断如果找不到这个id，那么和普通消息一样放入缓存区
    if (onLineStatus && type === 3) {
        onLineStatus.style.opacity = 1;
        offLineStatus.style.opacity = 0;
    }
    else if (offLineStatus && type === 4) {
        onLineStatus.style.opacity = 0;
        offLineStatus.style.opacity = 1;
    }
    else {
        doMessageNotice(content, from, to, type, time, message);
    }
}

//列出好友列表

function listRelation() {
    let allRelations = getAllRelations();
    let allGroups = getUserAllGroups();
    let html = '<div id="relationList" class="relation">' +
        '<div class="lists">' +
        '<ul class="nav nav-tabs">' +
        '<li class="active justifile text-center">' +
        '<a href="#relationPeople" data-toggle="tab">' +
        '联系列表' +
        '</a>' +
        '</li>' +
        '<li class="justifile text-center">' +
        '<a href="#groupPeople" data-toggle="tab">' +
        ' 群列表' +
        '</a>' +
        '</li>' +
        '</ul>' +
        '<div class="tab-content">' +
        '<div class="tab-pane fade in active" id="relationPeople">';
    for (let i = 0; i < allRelations.length; i++) {
        //注意！注意！注意！onclick后面调用的function里面的参数如果是字符串只能用单引号！！！！只能用单引号！
        //血和泪的教训搞了好久才改过来这里的错误，哎。之前使用单引号对了，这里改成ajax时因为字符串拼接是单引号所以里面就用了双引号，结果一直错错错，而且还不说错哪儿了，哎
        //现在想想也是，虽然js里面字符串单引号双引号都行，但是在html里面添加参数就不一样了！对于html来讲双引号是一个标签属性开始与结束的标志，而单引号没有意义，
        //所以如果这里用了双引号会引起html语意混淆，单引号则没有问题，记笔记，记笔记。（js拼接字符串时使用\+字符，这个字符就会作为普通的字符串被拼接上啦！不会被认为是有特殊含义的）
        html += '<div class="relationSingle" onclick="chatWithSomeBody(' + allRelations[i].userId + ',\'' + allRelations[i].userName + '\',\'' + allRelations[i].userNickName + '\');">' +
            '<div class="photoBox">' +
            '<img class="img-circle photo" src="img/photo.jpg">' +
            '</div>' +
            '<div id="currentNickName" class="list">' +
            allRelations[i].userNickName +
            '</div>' +
            '<div id="' + allRelations[i].userId + 'onlineStatus" class="onlineStatus">' +
            '在线' +
            '</div>' +
            '<div id="' + allRelations[i].userId + 'offlineStatus" class="offlineStatus">' +
            '离线' +
            '</div>' +
            '<div class="recent">' +
            '</div>' +
            '</div>';

    }
    html += '</div>' +
        '<div class="tab-pane fade" id="groupPeople">';
    for (let i = 0; i < allGroups.length; i++) {
        html += '<div class="relationSingle" onclick="chatWithGroup(' + allGroups[i].id + ',\'' + allGroups[i].groupId + '\',\'' + allGroups[i].groupName + '\',' + allGroups[i].groupCreatorId + ');">' +
            '<div class="photoBox">' +
            '<img class="img-circle photo" src="img/photo.jpg"/>' +
            '</div>' +
            '<div class="list">' +
            allGroups[i].groupName +
            '</div>' +
            '<div class="recent">' +
            '</div>' +
            '</div>';
    }
    html += '</div>' +
        '</div>' +
        '</div>' +
        '<div class="functions">' +
        '<div class="btn-group btn-group-justified">' +
        '<div class="btn-group">' +
        '<button type="button" class="btn btn-default" onclick="searchFriend();">添加好友</button>' +
        '</div>' +
        '<div class="btn-group">' +
        '<button type="button" class="btn btn-default" onclick="test();">测试</button>' +
        '</div>' +
        '<div class="btn-group">' +
        '<button type="button" class="btn btn-default">待定</button>' +
        '</div>' +
        '<div class="btn-group">' +
        '<button type="button" class="btn btn-default" onclick="createGroup()">创建群</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    relationListIndex = layer.open({
        type: 1,
        skin: 'layer-ext-lists',
        title: [currentUser.userNickName, 'font-family:Microsoft YaHei;font-size: 24px;height: 80px;'],
        closeBtn: 1,
        content: html,
        area: ['300px', '600px'],
        offset: 'rb',
        anim: 2,
        id: 'relationList',
        shade: 0,
        zIndex: layer.zIndex,
        success: function (layers) {
            layer.setTop(layers);
        }
    });
    for (let i = 0; i < allRelations.length; i++) {
        let onLineStatus = document.getElementById(allRelations[i].userId + 'onlineStatus');
        let offLineStatus = document.getElementById(allRelations[i].userId + 'offlineStatus');
        if (allRelations[i].userIsOnline === 0) {
            onLineStatus.style.opacity = 0;
            offLineStatus.style.opacity = 1;
        }
        else {
            onLineStatus.style.opacity = 1;
            offLineStatus.style.opacity = 0;
        }
    }
}




let relationApply = null;
let relationApplyNumber = 0;
function openRelationApply(content, from, to, type, time, message) {
    let fromUser = getUserById(from);
    let addFriendApply = document.getElementById('addFriendApply');
    if (!addFriendApply) {
        let html = '<div id="addFriendApply" class="addFriend">'
            + '<div id="friendApplyBox" class="container addFriendBox">'
            + '</div>'
            + '</div>';
        relationApply = layer.open({
            type: 1,
            title: '系统消息',
            content: html,
            area: ['500px', '275px'],
            shade: 0,
            resize: false,
            zIndex: layer.zIndex,
            success: function (layero) {
                layer.setTop(layero);
            }
        });
    }
    relationApplyNumber++;
    if (type === 5)
        var friendApplyHtml = '<div id="' + from + 'addFriendRow" class="row addFriendRow">'
            + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 addFriendImgBox">'
            + '<img src="img/photo.jpg" class="img-circle addFriendImg">'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '用&nbsp;&nbsp;户&nbsp;&nbsp;名：'
            + fromUser.userName
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：'
            + fromUser.userNickName
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '附加消息：'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + content
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'agreeAddThisUser(' + from + ')\'>加了这货</button>'
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'refuseAddThisUser(' + from + ')\'>残忍拒绝</button>'
            + '</div>'
            + '</div>';
    else if (type == 6) {
        let group = ajaxGetGroupById(content);
        friendApplyHtml = '<div id="' + group.id + 'addFriendRow" class="row addFriendRow">'
            + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 addFriendImgBox">'
            + '<img src="img/photo.jpg" class="img-circle addFriendImg">'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '群&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：'
            + group.groupId
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '群&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：'
            + group.groupName
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '类型：入群邀请'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + '邀请人：'
            + fromUser.userNickName
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'agreeGroupInvite(' + content + ')\'>进去看看</button>'
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'refuseGroupInvite(' + content + ')\'>不感兴趣</button>'
            + '</div>'
            + '</div>';
    }
    let friendApplyBox = document.getElementById('friendApplyBox');
    friendApplyBox.innerHTML += friendApplyHtml;
}
//测试
function test() {
    addFriend(1, '测试', '测试');
}