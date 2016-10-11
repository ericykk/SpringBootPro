package com.eric.spring.boot.action.dao.common;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 基础dao - 可读写
 * 
 * @author eric
 */
@Repository
@Lazy(true)
public class Dao extends SqlSessionDaoSupport {

	@Resource(name = "sqlSessionFactory")
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}
}
