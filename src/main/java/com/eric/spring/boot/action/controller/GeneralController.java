package com.eric.spring.boot.action.controller;

import com.eric.spring.boot.action.model.User;
import com.eric.spring.boot.action.service.GeneralService;
import com.eric.spring.boot.base.util.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description:
 * author:Eric
 * Date:16/10/11
 * Time:15:52
 * version 1.0.0
 */
@RestController
@RequestMapping(value = "/spring/boot")
public class GeneralController {
    @Autowired
    private GeneralService generalService;

    @RequestMapping(value = "/getNowDate",method = RequestMethod.GET)
    public String getNowDate(){
        return JSONParser.toJson(generalService.getNowDate());
    }

    @ResponseBody
    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    public User getUser(@RequestParam(value = "name") String name){
        return generalService.getUser(name).get(0);
    }
}
