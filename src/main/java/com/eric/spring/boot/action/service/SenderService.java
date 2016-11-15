package com.eric.spring.boot.action.service;

import com.eric.spring.boot.action.model.LoginUser;
import com.eric.spring.boot.action.model.User;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description:
 * author:Eric
 * Date:16/11/15
 * Time:16:29
 * version 1.0.0
 */
@Service
public class SenderService {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendUser2Rabbitmq(final User user) {
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue.user", user);
    }

    public void sendLoginUser2Rabbitmq(final LoginUser loginUser){
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue.loginUser", loginUser);
    }

}
