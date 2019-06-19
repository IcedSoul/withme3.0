//下面是关于群组的窗口和操作

//获取某人的所有群
function getUserAllGroups() {
    let allGroups = [];
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/users/groups/' + currentUser.userId,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                allGroups = result.content;
            }
            else {
                layer.alert(result.message());
            }
        },
        error: function (result) {
            layer.alert('查询错误');
        }
    });
    //划重点划重点，这里的eval方法不同于prase方法，外面加括号
    if(allGroups !== ""){
        allGroups = eval("(" + allGroups + ")");
    }
    return allGroups;
}

//获取某群的所有人
function getGroupAllUsers(id) {
    let usersAndUserGroup = {};
    let response = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/group/users/' + id,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.alert('查询错误');
        }
    });
    if (response.status == 1) {
        let content = JSON.parse(response.content);
        usersAndUserGroup.userGroups = content.userGroups;
        usersAndUserGroup.users = content.users;
    }
    else {
        layer.alert(response.message);
    }
    usersAndUserGroup.userGroups = eval("(" + usersAndUserGroup.userGroups + ")");
    usersAndUserGroup.users = eval("(" + usersAndUserGroup.users + ")");
    return usersAndUserGroup;
}

//创建群的窗口
let createGroupIndex = null;

function createGroup() {
    //此处留待添加群头像上传的功能
    let html = '<div class="createGroup">'
        + '<div class="container createGroupBox">'
        + '<div class="row createGroupRow">'
        + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 createGroupImgBox">'
        + '<img src="img/photo.jpg" class="img-circle createGroupImg">'
        + '</div>'
        + '<div class="col-md-2 col-sm-2 col-xs-2 col-lg-2 createGroupInfo">'
        + '群&nbsp;&nbsp;名&nbsp;&nbsp;称：'
        + '</div>'
        + '<div class="col-md-4 col-sm-4 col-xs-4 col-lg-4 createGroupInfo">'
        + '<input id="createGroupName" type="text" class="form-control" placeholder="作死小分队">'
        + '</div>'
        + '<div class="col-md-6 col-sm-6 col-xs-6 col-lg-6 createGroupInfo">'
        + '群描述：'
        + '<textarea id="createGroupIntroduction" class="form-control createGroupTextArea" placeholder="生命不息，作死不止！" rows="2"></textarea>'
        + '</div>'
        + '<div class="col-md-6 col-sm-6 col-xs-6 col-lg-6 createGroupInfo">'
        + '<button class="btn btn-default" onclick="ajaxCreateGroup()">建立群组</button>'
        + '</div>'
        + '</div>'
        + '</div>'
        + '</div>';
    createGroupIndex = layer.open({
        type: 1,
        title: '创建群组',
        content: html,
        area: ['500px', '275px'],
        resize: false,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });
}

//创建群的操作
function ajaxCreateGroup() {
    if (createGroupIndex != null) {
        let createGroup = {};
        createGroup.groupName = document.getElementById('createGroupName').value;
        createGroup.groupIntroduction = document.getElementById('createGroupIntroduction').value;
        createGroup.groupCreatorId = currentUser.userId;
        layer.close(createGroupIndex);
        let response = null;
        $.ajax({
            async: false, //设置同步
            type: 'POST',
            url: address + 'v1/group',
            data: createGroup,
            dataType: 'json',
            success: function (result) {
                response = result;
            },
            error: function (result) {
                layer.msg('创建群组发生错误', {
                    icon: 2,
                    zIndex: 20000001,
                    time: 2000
                });
            }
        });
        if (response.status == 1) {
            layer.alert('创建群组成功,你的群id为：' + JSON.parse(response.content).groupId);
        } else {
            layer.msg('创建群组失败', {
                icon: 2,
                zIndex: 20000001,
                time: 2000
            });
        }
    }
    else {
        layer.msg('创建群组发出的请求参数不完整，请重新创建', {
            icon: 2,
            zIndex: 20000001,
            time: 2000
        });
    }
}

//ajax获取单个用户与群组的聊天记录
function getMessageRecordBetweenUserAndGroup(id, page, number) {
    let response = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/groupMessages/' + currentUser.userId + "/" + id + "/"+ page +"/" + number,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.alert('查询错误');
        }
    });
    if (response.status == 1) {
        let allMessages = response.content;
        allMessages = eval("(" + allMessages + ")");
        return allMessages;
    }
    else {
        layer.alert(response.content);
        return null;
    }
}

//群组聊天窗口
function chatWithGroup(id, groupId, groupName, groupCreatorId) {
    let chatWith = '<div class="chatWith">'
        + '<div id="' + id + 'outputGroup" class="container output">'
        + '</div>'
        + '<hr/>'
        + '<div class="options">'
        + '<img class="option" src="img/face.png">'
        + '<img class="option" src="img/img.png">'
        + '<button type="button" class="btn btn-default pull-right" onclick="sendGroupMessagePre('
        + id
        + ')">发送</button>'
        + '<button type="button" class="btn btn-default pull-right smallOffset" onclick="groupUserList(' + id + ')">查看群成员</button>';
    if (groupCreatorId == currentUser.userId)
        chatWith += '<button type="button" class="btn btn-default pull-right smallOffset" onclick="groupInvite(' + id + ')">邀请新成员</button>';
    chatWith += '</div>'
        + '<div class="input">'
        + '<textarea id="'
        + id
        + 'messageTextGroup" class="form-control" onkeydown="binfEnterGroup(event,'
        + id + ')"></textarea>' + '</div>' + '</div>';
    layer.open({
        type: 1,
        title: [groupName, 'font-family:Microsoft YaHei;font-size: 24px;height: 80px;'],
        content: chatWith,
        area: ['600px', '600px'],
        maxmin: true,
        offset: ['100px', '100px'],
        shade: 0,
        id: groupId,
        resize: true,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });

    //获取消息记录
    let allMessages = getMessageRecordBetweenUserAndGroup(id, 0, 30);
    for (let i = 0; i < allMessages.length; i++) {
        let usersId = new Array();
        usersId[0] = id;
        usersId[1] = allMessages[i].toId;
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

//监听群聊输入框回车键
function binfEnterGroup(obj, id) {
    if (obj.keyCode == 13) {
        sendGroupMessagePre(id);
        obj.preventDefault();
    }
}

function ajaxGetGroupById(id) {
    let group = null;
    let response = null;
    $.ajax({
        async: false, //设置同步
        type: 'GET',
        url: address + 'v1/group/' + id,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.msg('打开群组发生错误', {
                icon: 2,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
    if (response.status === 1) {
        group = eval("(" + response.content + ")");
    }
    else {
        layer.msg('打开群组失败', {
            icon: 2,
            zIndex: 20000001,
            time: 2000
        });
    }
    return group;
}

//发送群聊消息传递准备工作
function sendGroupMessagePre(id) {
    let group = ajaxGetGroupById(id);

    let textAreaId = id + 'messageTextGroup';
    let message = document.getElementById(textAreaId).value;
    if (message !== '') {
        let toUsersIdString = (group.groupMembers).split(',');
        let toUsersId = [];
        toUsersId[0] = group.id;
        for (let i = 0; i < toUsersIdString.length; i++) {
            toUsersId[i + 1] = parseInt(toUsersIdString[i]);
        }
        sendMessage(message, toUsersId, 1);
        document.getElementById(textAreaId).value = '';
    }
}

//加群邀请好友名单
function groupInvite(id) {
    let allRelations = getAllRelations();
    let html = '<div class="groupInviteList">' +
        '<table class="table table-striped table-hover">' +
        '<tr>' +
        '<th>#</th>' +
        '<th>用户名</th>' +
        '<th>昵称</th>' +
        '<th>邀请</th>' +
        '</tr>';
    for (let i = 0; i < allRelations.length; i++) {
        html += '<tr>' +
            '<td>' +
            (i + 1) +
            '</td>' +
            '<td>' + allRelations[i].userName + '</td>' +
            '<td>' + allRelations[i].userNickName + '</td>' +
            '<td>' +
            '<button type="button" class="btn btn-default" onclick="groupInviteUser(' + id + ',' + allRelations[i].userId + ')">邀请入群</button>' +
            '</td>' +
            '</tr>';
    }
    html += '</table>' +
        '</div>';
    layer.open({
        type: 1,
        title: '邀请好友入群',
        content: html,
        area: ['400px', '600px'],
        resize: false,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });
}

function groupInviteUser(id, userId) {
    let message = id;
    let toUsersId = new Array();
    toUsersId[0] = userId;
    sendMessage(message, toUsersId, 6);
}

//同意加群邀请
function agreeGroupInvite(id) {
    let addGroup = {};
    addGroup.id = id;
    addGroup.userId = currentUser.userId;
    let response = null;
    $.ajax({
        async: false, //设置同步
        type: 'POST',
        url: address + 'v1/group/user',
        data: addGroup,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.msg('添加失败', {
                icon: 6,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
    if (response.status == 1) {
        layer.close(relationListIndex);
    }
    // else {
    //     text = '是上天不允许我们建立联系啊！';
    // }
    let addFriendRow = document.getElementById(id + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber == 0)
        layer.close(relationApply);
}

//拒绝加群邀请
function refuseGroupInvite(id) {
    let addFriendRow = document.getElementById(id + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber == 0)
        layer.close(relationApply);
}

//列出群成员
function groupUserList(id) {
    let usersAndUserGroup = getGroupAllUsers(id);
    let users = usersAndUserGroup.users;
    let userGroup = usersAndUserGroup.userGroups;
    let html = '<div class="groupInviteList">' +
        '<table class="table table-striped table-hover">' +
        '<tr>' +
        '<th>#</th>' +
        '<th>用户名</th>' +
        '<th>昵称</th>' +
        '<th>群昵称</th>' +
        '<th>等级</th>' +
        '<th>进群时间</th>' +
        '</tr>';
    for (let i = 0; i < users.length; i++) {
        html += '<tr>' +
            '<td>' +
            (i + 1) +
            '</td>' +
            '<td>' + users[i].userName + '</td>' +
            '<td>' + users[i].userNickName + '</td>' +
            '<td>' + userGroup[i].groupUserNickName + '</td>' +
            '<td>' + userGroup[i].groupLevel + '</td>' +
            '<td>' + userGroup[i].enterGroupTime + '</td>' +
            '</tr>';
    }
    html += '</table>' +
        '</div>';
    layer.open({
        type: 1,
        title: '群组成员名单',
        content: html,
        area: ['500px', '600px'],
        resize: false,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });
}