package com.eric.spring.boot.test;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:日志测试类
 * author:Eric
 * Date:16/10/28
 * Time:10:50
 * version 1.0.0
 */
public class LogTest extends BaseTest{

    private Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void testLog(){
        logger.debug("测试debug级别日志");
        logger.info("测试info级别日志");
        logger.error("测试error级别日志");
    }
}
