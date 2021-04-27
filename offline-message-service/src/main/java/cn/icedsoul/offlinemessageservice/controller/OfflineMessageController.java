package cn.icedsoul.offlinemessageservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.offlinemessageservice.service.serviceApi.OfflineMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "离线消息控制类", description = "用来处理离线消息的增删改查等")
@RequestMapping(value = "/v1")
public class OfflineMessageController {
    @Autowired
    OfflineMessageService offlineMessageService;

    @ApiOperation(value = "Add OfflineMessage Message Record", notes = "添加离线聊天消息", response = Response.class)
    @PostMapping(value = "/offlineMessage")
    public Response addOfflineMessage(@RequestParam("fromId")Integer fromId, @RequestParam("toId")Integer toId,
                               @RequestParam("content") String content, @RequestParam("type")Integer type,
                               @RequestParam("time") String time){
        return offlineMessageService.addOfflineMessage(fromId, toId, content, type, time);
    }

    @ApiOperation(value = "Get OfflineMessage Message", notes = "添加离线聊天消息", response = Response.class)
    @GetMapping(value = "/offlineMessages/toId/{userId}")
    public Response getOfflineMessage(@PathVariable("userId")Integer userId){
        return offlineMessageService.getOfflineMessageTo(userId);
    }

//    @ApiOperation(value = "Add OfflineMessage Message Record", notes = "添加离线聊天消息", response = Response.class)
//    @GetMapping(value = "/offlineMessages/toId/{userId}")
//    public Response addMessage(@PathVariable("userId")Integer userId){
//        return offlineMessageService.getOfflineMessageTo(userId);
//    }
}
