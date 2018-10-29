package cn.icedsoul.withmeservice.controller;

import cn.icedsoul.withmeservice.service.ServiceAPI.UserService;
import cn.icedsoul.withmeservice.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IcedSoul on 2018/2/20.
 */
@RestController
@Api(value = "用户控制类",description = "用来处理用户注册、登录以及增删改查等")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "用户登录接口",notes = "处理用户登录",response = Response.class)
    @PostMapping(value = "/login")
    public Response login(@RequestParam("jsonObj") String jsonObj){
        return userService.login(jsonObj);
    }

    @ApiOperation(value = "用户退出登录接口",notes = "退出用户登录",response = Response.class)
    @PostMapping(value = "/logout")
    public Response logout(@RequestParam("jsonObj") String jsonObj){
        return userService.logout(jsonObj);
    }

    @ApiOperation(value = "用户注册接口",notes = "处理用户注册",response = Response.class)
    @PostMapping(value = "/register")
    public Response register(@RequestParam("jsonObj") String jsonObj){
        return userService.register(jsonObj);
    }

    @ApiOperation(value = "获取用户好友",notes = "获取用户所有好友",response = Response.class)
    @PostMapping(value = "/getRelations")
    public Response getRelations(@RequestParam("jsonObj") String jsonObj){
        return userService.getRelations(jsonObj);
    }

    @ApiOperation(value = "获取用户群组",notes = "获取用户所有群组",response = Response.class)
    @PostMapping(value = "/getUserGroups")
    public Response getUserGroups(@RequestParam("jsonObj") String jsonObj){
        return userService.getUserGroups(jsonObj);
    }

    @ApiOperation(value = "获取当前用户ID",notes = "获取当前用户ID",response = Response.class)
    @PostMapping(value = "/getCurrentUser")
    public Response getCurrentUser(@RequestParam("jsonObj") String jsonObj){
        return userService.getCurrentUser(jsonObj);
    }

    @ApiOperation(value = "根据用户名获取当前用户",notes = "根据用户名获取当前用户ID",response = Response.class)
    @PostMapping(value = "/findUserByName")
    public Response findUserByName(@RequestParam("jsonObj") String jsonObj){
        return userService.findUserByName(jsonObj);
    }

    @ApiOperation(value = "根据用户名获取当前用户",notes = "根据用户名获取当前用户ID",response = Response.class)
    @PostMapping(value = "/findUserById")
    public Response findUserById(@RequestParam("jsonObj") String jsonObj){
        return userService.findUserById(jsonObj);
    }

}
