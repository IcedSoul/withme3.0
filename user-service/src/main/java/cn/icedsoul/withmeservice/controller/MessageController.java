package cn.icedsoul.withmeservice.controller;

import cn.icedsoul.withmeservice.service.ServiceAPI.MessageService;
import cn.icedsoul.withmeservice.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "消息控制类",description = "用来处理消息的增删改查等")
@RequestMapping(value = "/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @ApiOperation(value = "获取用户之间的聊天记录",notes = "获取两个用户之间的聊天记录",response = Response.class)
    @PostMapping(value = "/getMessageRecordBetweenUsers")
    public Response getMessageRecordBetweenUsers(@RequestParam("jsonObj") String jsonObj){
        return messageService.getMessageRecordBetweenUsers(jsonObj);
    }

    @ApiOperation(value = "获取用户与群组之间的聊天记录",notes = "获取用户与群组之间的聊天记录",response = Response.class)
    @PostMapping(value = "/getMessageRecordBetweenUserAndGroup")
    public Response getMessageRecordBetweenUserAndGroup(@RequestParam("jsonObj") String jsonObj){
        return messageService.getMessageRecordBetweenUserAndGroup(jsonObj);
    }
}
