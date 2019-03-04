package cn.icedsoul.messageservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.messageservice.domain.Message;
import cn.icedsoul.messageservice.service.serviceApi.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 2019.01.04
 * 拆分出message-service，并且将接口修改为restful风格，进行彻底重写
 *
 * 因为设计方面的问题，部分旧有接口删除或者暂且保留以待修改
 *
 * 2019.03.01
 *
 * 群组表和离线消息表都已经添加，这里
 *
 */

@RestController
@Api(value = "消息控制类", description = "用来处理消息的增删改查等")
@RequestMapping(value = "/v1")
public class MessageController {
    @Autowired
    MessageService messageService;

    @ApiOperation(value = "Add Message Record", notes = "添加聊天记录", response = Response.class)
    @PostMapping(value = "/message")
    public Response addMessage(@RequestParam("fromId")Integer fromId, @RequestParam("toId")Integer toId,
                            @RequestParam("content") String content, @RequestParam("type")Integer type,
                            @RequestParam("time") String time){
        return messageService.addMessage(fromId, toId, content, type, time);
    }


    @ApiOperation(value = "Get message records between user A and user B", notes = "获取两个用户之间的聊天记录", response = Response.class)
    @GetMapping(value = "/messages")
    public Response getMessageRecordBetweenUsers(@RequestParam(value = "userIdA") Integer userIdA, @RequestParam("userIdB") Integer userIdB) {
        return messageService.getMessageRecordBetweenUsers(userIdA, userIdB);
    }

    /**
     * 批量添加聊天记录
     * @param messages
     * @return
     */
    @ApiOperation(value = "Add Message Records", notes = "add Message Records", response = Response.class)
    @PostMapping(value = "/messages")
    public Response updateMessage(@RequestParam("messages")List<Message> messages){
        return messageService.addMessages(messages);
    }

//    /**
//     * 关于群组聊天的记录，应该对其专门作出优化，比如：
//     * 专门为群组聊天建立一张聊天记录表，这样也方便查询和插入，所以这里暂且保留，不做修改
//     *
//     * 群组表已经建立，这里删除
//     * @param jsonObj
//     * @return
//     */
//    @ApiOperation(value = "获取用户与群组之间的聊天记录", notes = "获取用户与群组之间的聊天记录", response = Response.class)
//    @PostMapping(value = "/getMessageRecordBetweenUserAndGroup")
//    public Response getMessageRecordBetweenUserAndGroup(@RequestParam("jsonObj") String jsonObj) {
//        return messageService.getMessageRecordBetweenUserAndGroup(jsonObj);
//    }
}
