package com.eric.spring.boot.action.service;

import com.eric.spring.boot.action.dao.GeneralDao;
import com.eric.spring.boot.action.model.User;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * description:
 * author:Eric
 * Date:16/10/11
 * Time:15:48
 * version 1.0.0
 */
@Service
public class GeneralService {
    @Autowired
    private GeneralDao generalDao;


    public Date getNowDate(){
        return  generalDao.getDate();
    }

    public List<User> getUser(String name){
        return  generalDao.getUser(name);
    }
}
