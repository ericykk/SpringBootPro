package com.eric.spring.boot.base.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import javax.annotation.Resource;

/**
 * description:RabbitMQ 生产者
 * author:Eric
 * Date:16/11/15
 * Time:15:26
 * version 1.0.0
 */
@Configuration
public class ProducerConfig {


    @Resource(name = "connectionFactory")
    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    Queue queueUser(RabbitAdmin rabbitAdmin) {
        Queue queue = new Queue("queue.user", true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    Queue queueLoginUser(RabbitAdmin rabbitAdmin) {
        Queue queue = new Queue("queue.loginUser", true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    TopicExchange exchange(RabbitAdmin rabbitAdmin) {
        TopicExchange topicExchange = new TopicExchange("exchange");
        rabbitAdmin.declareExchange(topicExchange);
        return topicExchange;
    }

    @Bean
    Binding bindingExchangeFoo(Queue queueUser, TopicExchange exchange, RabbitAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(queueUser).to(exchange).with("queue.user");
        rabbitAdmin.declareBinding(binding);
        return binding;
    }

    @Bean
    Binding bindingExchangeBar(Queue queueLoginUser, TopicExchange exchange,RabbitAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(queueLoginUser).to(exchange).with("queue.loginUser");
        rabbitAdmin.declareBinding(binding);
        return binding;
    }



    /**
     * 生产者用
     * @return
     */
    @Resource(name = "rabbitTemplate")
    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(jackson2Converter());
        rabbitMessagingTemplate.setRabbitTemplate(rabbitTemplate);
        return rabbitMessagingTemplate;
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }
}
