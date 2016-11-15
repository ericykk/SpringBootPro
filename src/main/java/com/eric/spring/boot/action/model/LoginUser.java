package com.eric.spring.boot.action.model;

/**
 * description:
 * author:Eric
 * Date:16/10/11
 * Time:16:29
 * version 1.0.0
 */
public class LoginUser {
    private int id;
    private String name;
    private String sex;

    public LoginUser(){

    }

    public LoginUser(String name,String sex){
        this.name = name;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
