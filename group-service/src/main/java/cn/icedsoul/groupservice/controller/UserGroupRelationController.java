package cn.icedsoul.groupservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupservice.service.serviceApi.UserGroupRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户控制类", description = "用来处理用户注册、登录以及增删改查等")
@RequestMapping(value = "/userGroup")
public class UserGroupRelationController {
    @Autowired
    UserGroupRelationService userGroupRelationService;

    @ApiOperation(value = "加群", notes = "群组添加新成员", response = Response.class)
    @PostMapping(value = "/addGroupUsers")
    public Response addGroupUsers(@RequestParam("jsonObj") String jsonObj) {
        return userGroupRelationService.addGroupUsers(jsonObj);
    }
}
