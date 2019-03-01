package cn.icedsoul.groupmessageservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupmessageservice.service.serviceApi.GroupMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 2019.01.04
 * 拆分出message-service，并且将接口修改为restful风格，进行彻底重写
 *
 * 因为设计方面的问题，部分旧有接口删除或者暂且保留以待修改
 *
 */

@RestController
@Api(value = "the controller of group message", description = "providing operational interface for group message table")
@RequestMapping(value = "/v1")
public class GroupMessageController {
    @Autowired
    GroupMessageService groupMessageService;

    @ApiOperation(value = "Add GroupMessage Record", notes = "Add GroupMessage Record", response = Response.class)
    @PostMapping(value = "/groupMessage")
    public Response addMessage(@RequestParam("fromId")Integer fromId, @RequestParam("fromId")Integer groupId,
                               @RequestParam("toId")String toId, @RequestParam("content") String content,
                               @RequestParam("type")Integer type, @RequestParam("time") String time,
                               @RequestParam("isTransport")Integer isTransport){
        return groupMessageService.addGroupMessage(fromId,groupId, toId, content, type, time, isTransport);
    }


    @ApiOperation(value = "Get message records between user A and Group B", notes = "Get message records between user A and Group B", response = Response.class)
    @GetMapping(value = "/groupMessages")
    public Response getMessageRecordBetweenUsers(@RequestParam(value = "userId") Integer userId, @RequestParam("groupId") Integer groupId, @RequestParam("limit") Integer limit) {
        return groupMessageService.getMessageRecordBetweenUserAndGroup(userId, groupId, limit);
    }


    /**
     * 关于群组聊天的记录，应该对其专门作出优化，比如：
     * 专门为群组聊天建立一张聊天记录表，这样也方便查询和插入，所以这里暂且保留，不做修改
     * @param jsonObj
     * @return
     */
//    @ApiOperation(value = "获取用户与群组之间的聊天记录", notes = "获取用户与群组之间的聊天记录", response = Response.class)
//    @PostMapping(value = "/getMessageRecordBetweenUserAndGroup")
//    public Response getMessageRecordBetweenUserAndGroup(@RequestParam("jsonObj") String jsonObj) {
//        return messageService.getMessageRecordBetweenUserAndGroup(jsonObj);
//    }
}
