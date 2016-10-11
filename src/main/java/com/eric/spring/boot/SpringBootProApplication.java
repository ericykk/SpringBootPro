package com.eric.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBoot启动类
 */
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class SpringBootProApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProApplication.class,args);
    }

}