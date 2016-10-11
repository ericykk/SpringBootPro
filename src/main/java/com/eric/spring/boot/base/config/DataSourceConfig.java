package com.eric.spring.boot.base.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * 数据源配置
 * @author eric
 */
@Configuration
public class DataSourceConfig {

    /**
     * 主数据源，必须配置，spring启动时会执行初始化数据操作（无论是否真的需要）
     * @return
     */
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 配置sqlSessionFactory
     * @return SqlSessionFactoryBean
     * @author eric
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(this.dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setConfigLocation(resolver.getResource("mybatis-config.xml"));
        try {
            factory.setMapperLocations(resolver.getResources("mappers/**/*.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return factory;
    }


    /**
     * 配置事务
     * @param dataSource 数据源
     * @return DataSourceTransactionManager
     * @author eric
     */
    @Resource(name = "datasource")
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
        return manager;
    }

}
