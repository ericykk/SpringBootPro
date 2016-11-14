package com.eric.spring.boot.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 环境变量配置
 */
@Component
public class EnvConfig {


    /**
     * 当前启动环境
     */
    @Value("${spring.profiles.active}")
    private String active;

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

}