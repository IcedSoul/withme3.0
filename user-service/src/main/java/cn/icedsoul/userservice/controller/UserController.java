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
 * 登录、注册暂时留在user-service之内。之后应单独抽离出来，留下接口认证（也方便第三方直接使用）
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
     * @param jsonObj
     * @return
     */
    @ApiOperation(value = "用户登录接口", notes = "处理用户登录", response = Response.class)
    @PostMapping(value = "/login")
    public Response login(@RequestParam("jsonObj") String jsonObj) {
        return userService.login(jsonObj);
    }

    @ApiOperation(value = "用户退出登录接口", notes = "退出用户登录", response = Response.class)
    @PostMapping(value = "/logout")
    public Response logout(@RequestParam("jsonObj") String jsonObj) {
        return userService.logout(jsonObj);
    }

    @ApiOperation(value = "用户注册接口", notes = "处理用户注册", response = Response.class)
    @PostMapping(value = "/register")
    public Response register(@RequestParam("jsonObj") String jsonObj) {
        return userService.register(jsonObj);
    }


    @ApiOperation(value = "获取用户好友", notes = "获取用户所有好友", response = Response.class)
    @GetMapping(value = "/users/friends")
    public Response getRelations(@RequestParam("userId") Integer userId) {
        return userService.getRelations(userId);
    }

    @ApiOperation(value = "获取用户群组", notes = "获取用户所有群组", response = Response.class)
    @PostMapping(value = "/users/friends")
    public Response getUserGroups(@RequestParam("userId") Integer userId) {
        return userService.getUserGroups(userId);
    }

    @ApiOperation(value = "获取当前用户ID", notes = "获取当前用户ID", response = Response.class)
    @PostMapping(value = "/getCurrentUser")
    public Response getCurrentUser(@RequestParam("jsonObj") String jsonObj) {
        return userService.getCurrentUser(jsonObj);
    }

    @ApiOperation(value = "根据用户名获取当前用户", notes = "根据用户名获取当前用户ID", response = Response.class)
    @PostMapping(value = "/findUserByName")
    public Response findUserByName(@RequestParam("jsonObj") String jsonObj) {
        return userService.findUserByName(jsonObj);
    }

    @ApiOperation(value = "根据用户名获取当前用户", notes = "根据用户名获取当前用户ID", response = Response.class)
    @PostMapping(value = "/findUserById")
    public Response findUserById(@RequestParam("jsonObj") String jsonObj) {
        return userService.findUserById(jsonObj);
    }

}
