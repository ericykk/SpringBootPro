package com.eric.spring.boot.base.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description:Druid注册servlet和过滤器监控
 * author:Eric
 * Date:16/10/28
 * Time:14:46
 * version 1.0.0
 */
@Configuration
public class DruidConfig {

    /**
     * 注册servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletReg = new ServletRegistrationBean();
        servletReg.setServlet(new StatViewServlet());
        servletReg.addUrlMappings("/druid/*");
        servletReg.addInitParameter("allow", "127.0.0.1,10.73.181.25");//IP白名单 不配置 默认都可以访问
        servletReg.addInitParameter("deny","192.168.3.45");//IP黑名单  同时存在allow和deny中 则deny优先级高
        servletReg.addInitParameter("loginUsername", "admin");
        servletReg.addInitParameter("loginPassword", "admin");
        servletReg.addInitParameter("resetEnable","false");//禁用HTML页面上的“Reset All”功能
        return servletReg;
    }

    /**
     * 注册过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"); //忽略过滤的资源
        return filterRegistrationBean;
    }
}
