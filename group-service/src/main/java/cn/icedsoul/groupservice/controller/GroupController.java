package cn.icedsoul.groupservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupservice.service.serviceApi.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "群组控制类", description = "用来处理群组相关增删改查等")
@RequestMapping(value = "/v1")
public class GroupController {

    @Autowired
    GroupService groupService;

    @ApiOperation(value = "获取群组所有用户", notes = "获取群组的所有用户", response = Response.class)
    @GetMapping(value = "/group/users/{id}")
    public Response getGroupUsers(@PathVariable("id") Integer id) {
        return groupService.getGroupUsers(id);
    }

    @ApiOperation(value = "创建群组", notes = "创建群组", response = Response.class)
    @PostMapping(value = "/group")
    public Response createGroup(@RequestParam("groupName") String groupName,
                                @RequestParam("groupIntroduction") String groupIntroduction,
                                @RequestParam("groupCreatorId") Integer groupCreatorId) {
        return groupService.createGroup(groupName, groupIntroduction, groupCreatorId);
    }

    @ApiOperation(value = "根据id获取群组", notes = "根据id获取群组", response = Response.class)
    @GetMapping(value = "/group/{id}")
    public Response findGroupById(@PathVariable("id") Integer id) {
        return groupService.findGroupById(id);
    }
}
