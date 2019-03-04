package cn.icedsoul.offlinemessageservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.offlinemessageservice.service.serviceApi.OfflineGroupMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "群组离线消息控制类", description = "用来处理群组离线消息的增删改查等")
@RequestMapping(value = "/v1")
public class OfflineGroupMessageController {
    @Autowired
    OfflineGroupMessageService offlineGroupMessageService;

    @ApiOperation(value = "Add OfflineMessage Message Record", notes = "添加离线聊天消息", response = Response.class)
    @PostMapping(value = "/offlineGroupMessage")
    public Response addOfflineMessage(@RequestParam("fromId")Integer fromId, @RequestParam("groupId")Integer groupId,
                               @RequestParam("toId")Integer toId, @RequestParam("content") String content,
                               @RequestParam("type")Integer type, @RequestParam("time") String time){
        return offlineGroupMessageService.addOfflineGroupMessage(fromId, groupId, toId, content, type, time);
    }

}
