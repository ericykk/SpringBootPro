package com.eric.spring.boot.test;

import com.eric.spring.boot.SpringBootProApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * description:基础测试类
 * author:Eric
 * Date:16/10/28
 * Time:11:05
 * version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootProApplication.class)
@WebAppConfiguration
public class BaseTest {

}
