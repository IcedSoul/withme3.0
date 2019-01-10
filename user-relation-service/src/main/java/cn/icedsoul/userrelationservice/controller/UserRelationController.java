package cn.icedsoul.userrelationservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.userrelationservice.service.serviceApi.UserRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "用户关系控制类", description = "用来处理用户好友关系增删改查等")
@RequestMapping(value = "/v1")
public class UserRelationController {
    @Autowired
    UserRelationService userRelationService;

    @ApiOperation(value = "用户添加好友", notes = "添加好友关系", response = Response.class)
    @PostMapping(value = "/userRelations")
    public Response buildRelation(@RequestParam("userIdA") Integer userIdA, @RequestParam("userIdB") Integer userIdB) {
        return userRelationService.buildRelation(userIdA, userIdB);
    }


}
