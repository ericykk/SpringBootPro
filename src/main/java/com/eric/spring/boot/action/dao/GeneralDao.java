package com.eric.spring.boot.action.dao;

import com.eric.spring.boot.action.dao.common.BaseDaoManagement;
import com.eric.spring.boot.action.model.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author:Eric
 * Date:16/10/11
 * Time:15:35
 * version 1.0.0
 */
@Repository
public class GeneralDao extends BaseDaoManagement{

    /**
     * 获取当前时间
     * @return
     */
    public Date getDate(){
        return this.getSqlSession().selectOne("GeneralMapper.getGeneralDate");
    }

    /**
     * 获取用户信息
     * @param name
     * @return
     */
    public List<User> getUser(String name){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        return this.getSqlSession().selectList("GeneralMapper.getUser",paramMap);
    }

}
