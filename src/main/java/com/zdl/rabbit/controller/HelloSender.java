package com.zdl.rabbit.controller;

import com.zdl.rabbit.entity.UserInfo;
import com.zdl.rabbit.provider.MsgDelayedSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoudeli
 */
@RestController
@Slf4j
public class HelloSender {
    @Autowired
    private MsgDelayedSender msgDelayedSender;

    @GetMapping("/test")
    public String test() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("15800000000");

        log.info("开始发送消息");
        msgDelayedSender.sendDelayed(userInfo, 10);
        return "成功";
    }
}
