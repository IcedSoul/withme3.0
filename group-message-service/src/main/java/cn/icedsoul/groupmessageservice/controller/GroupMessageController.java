package cn.icedsoul.groupmessageservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupmessageservice.service.serviceApi.GroupMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 2019.03.01 添加
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
                               @RequestParam("type")Integer type, @RequestParam("time") String time){
        return groupMessageService.addGroupMessage(fromId,groupId, toId, content, type, time);
    }


    @ApiOperation(value = "Get message records between user A and Group B", notes = "Get message records between user A and Group B", response = Response.class)
    @GetMapping(value = "/groupMessages")
    public Response getMessageRecordBetweenUsers(@RequestParam(value = "userId") Integer userId, @RequestParam("groupId") Integer groupId, @RequestParam("limit") Integer limit) {
        return groupMessageService.getMessageRecordBetweenUserAndGroup(userId, groupId, limit);
    }

}
