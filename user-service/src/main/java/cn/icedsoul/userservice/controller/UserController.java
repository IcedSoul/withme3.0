package cn.icedsoul.userservice.controller;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.userservice.service.serviceApi.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 首先将接口的风格改为Restful
 * 其次添加必要的接口，删除非必要的接口
 *
 * 登录、注册暂时留在user-service之内。之后应单独抽离出来，留下接口认证
 *
 * Created by IcedSoul on 2018/2/20.
 * Modified by IcedSoul on 2019/1/4
 */
@RestController
@Api(value = "用户控制类", description = "用来处理用户注册、登录以及增删改查等")
@RequestMapping(value = "/v1")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 暂时保留
     *
     * @param
     * @return
     */
    @ApiOperation(value = "用户登录接口", notes = "处理用户登录", response = Response.class)
    @GetMapping(value = "/user/login")
    public Response login(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword) {
        return userService.login(userName, userPassword);
    }

    @ApiOperation(value = "用户退出登录接口", notes = "退出用户登录", response = Response.class)
    @GetMapping(value = "/user/logout")
    public Response logout(@RequestParam("token") String token) {
        return userService.logout(token);
    }

    @ApiOperation(value = "用户注册接口", notes = "处理用户注册", response = Response.class)
    @PostMapping(value = "/user/register")
    public Response register(@RequestParam("userName") String userName,
                             @RequestParam("userNickName") String userNickName,
                             @RequestParam("userPassword") String userPassword) {
        return userService.register(userName, userNickName, userPassword, 0);
    }

    @ApiOperation(value = "获取用户好友", notes = "获取用户所有好友", response = Response.class)
    @GetMapping(value = "/users/friends/{userId}")
    public Response getRelations(@PathVariable("userId") Integer userId) {
        return userService.getRelations(userId);
    }

    @ApiOperation(value = "获取用户群组", notes = "获取用户所有群组", response = Response.class)
    @GetMapping(value = "/users/groups/{userId}")
    public Response getUserGroups(@PathVariable("userId") Integer userId) {
        return userService.getUserGroups(userId);
    }

    @ApiOperation(value = "解析token获取当前用户", notes = "获取当前用户ID", response = Response.class)
    @GetMapping(value = "/user/token/{token}")
    public Response getCurrentUser(@PathVariable("token") String token) {
        return userService.getCurrentUser(token);
    }

    @ApiOperation(value = "根据用户名获取当前用户", notes = "根据用户名获取当前用户ID", response = Response.class)
    @GetMapping(value = "/user/userName/{userName}")
    public Response findUserByName(@PathVariable("userName") String userName) {
        return userService.findUserByName(userName);
    }

    @ApiOperation(value = "根据用户id获取当前用户", notes = "根据用户id获取当前用户", response = Response.class)
    @GetMapping(value = "/user/id/{id}")
    public Response findUserById(@PathVariable("id") String id) {
        return userService.findUserById(id);
    }

    @ApiOperation(value = "更新好友关系缓存字段", notes = "更新好友关系", response = Response.class)
    @PostMapping(value = "/user/userRelations")
    public Response updateUserRelation(@RequestParam("userIdA") Integer userIdA, @RequestParam("userIdB") Integer userIdB) {
        return userService.updateUserRelation(userIdA, userIdB);
    }

    @ApiOperation(value = "更新用户群组缓存字段", notes = "更新用户群组", response = Response.class)
    @PostMapping(value = "/user/userGroups")
    public Response updateUserGroup(@RequestParam("userId") Integer userId, @RequestParam("groupId") Integer groupId) {
        return userService.updateUserGroup(userId, groupId);
    }

    @ApiOperation(value = "根据id String获取用户List", notes = "获取用户List", response = Response.class)
    @GetMapping(value = "/users/{userIds}")
    public Response getUsersByUserIds(@PathVariable("userIds") String userIds) {
        return userService.getUsersByUserIds(userIds);
    }

}
