package com.eric.spring.boot.action.dao.common;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础Dao管理器配置
 * 可按照dao功能不同配置多个数据源
 * 
 * @author eric
 */
public abstract class BaseDaoManagement {

  @Autowired
  private Dao dao;

  /**
   * 获得正常的 sqlSession
   *
   * @return SqlSession
   * @author eric
   */
  protected SqlSession getSqlSession() {
    return this.dao.getSqlSession();
  }
}
