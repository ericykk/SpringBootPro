package com.eric.spring.boot.action.service;

import com.eric.spring.boot.action.model.LoginUser;
import com.eric.spring.boot.action.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * description:消息接收
 * author:Eric
 * Date:16/11/15
 * Time:16:16
 * version 1.0.0
 */
@Component
public class ReceiverService {

    @RabbitListener(queues = "queue.user")
    public void receiveUserQueue(User user) {
        System.out.println("Received User<" + user.getName() + ">");
    }

    @RabbitListener(queues = "queue.loginUser")
    public void receiveLoginUserQueue(LoginUser loginUser) {
        System.out.println("Received LoginUser<" + loginUser.getSex() + ">");
    }
}
