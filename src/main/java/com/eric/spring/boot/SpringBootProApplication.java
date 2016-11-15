package com.eric.spring.boot;

import com.eric.spring.boot.action.model.LoginUser;
import com.eric.spring.boot.action.model.User;
import com.eric.spring.boot.action.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringBoot启动类
 */
@SpringBootApplication //等同于同时配置@Configuration @EnableAutoConfiguration @ComponentScan
@ComponentScan
public class SpringBootProApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProApplication.class,args);
    }

    @Autowired
    SenderService senderService;

    @Override
    public void run(String... strings) throws Exception {
        for(int i=1;i<100;i++){
            senderService.sendLoginUser2Rabbitmq(new LoginUser("RabbitMq loginUser","男"));
            senderService.sendUser2Rabbitmq(new User("RabbitMq user","女"));
        }
    }

}
