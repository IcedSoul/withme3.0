package cn.icedsoul.withmeservice.controller;

import cn.icedsoul.withmeservice.service.ServiceAPI.GroupService;
import cn.icedsoul.withmeservice.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "群组控制类",description = "用来处理群组相关增删改查等")
@RequestMapping(value = "/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @ApiOperation(value = "获取群组所有用户",notes = "获取群组的所有用户",response = Response.class)
    @PostMapping(value = "/getGroupUsers")
    public Response getGroupUsers(@RequestParam("jsonObj") String jsonObj){
        return groupService.getGroupUsers(jsonObj);
    }

    @ApiOperation(value = "创建群组",notes = "创建群组",response = Response.class)
    @PostMapping(value = "/createGroup")
    public Response createGroup(@RequestParam("jsonObj") String jsonObj){
        return groupService.createGroup(jsonObj);
    }

    @ApiOperation(value = "根据id获取群组",notes = "根据id获取群组",response = Response.class)
    @PostMapping(value = "/findGroupById")
    public Response findGroupById(@RequestParam("jsonObj") String jsonObj){
        return groupService.findGroupById(jsonObj);
    }
}
