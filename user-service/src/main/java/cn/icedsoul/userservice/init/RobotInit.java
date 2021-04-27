package cn.icedsoul.userservice.init;

import cn.icedsoul.userservice.service.serviceApi.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static cn.icedsoul.userservice.constant.CONSTANT.ROBOT_NAME;
import static cn.icedsoul.userservice.constant.CONSTANT.ROBOT_NICK_NAME;

/**
 * @author xiaofeng
 * @date 2021/4/27
 */
@Component
public class RobotInit implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.register(ROBOT_NAME, ROBOT_NICK_NAME, "unKnownPassword", 1);
    }
}
