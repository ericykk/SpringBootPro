package com.eric.spring.boot.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/** 
 * JSON工具
 */
public class JSONParser {

    /**
     * 将json转化成Bean，支持深度转化
     * @param json      源json字符串
     * @param clazz     目标Bean类型
     * @param <T>       转化结果泛型
     * @return          转化后的对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return t;
    }

    /**
     * 将json转化成list
     * @param json      源json字符串
     * @param type      list的类型
     * @param <T>       转化后核心泛型
     * @return          list
     */
    public static <T> List<T> toList(String json, TypeReference<List<T>> type) {
        ObjectMapper mapper = new ObjectMapper();
        List<T> list = null;
        try {
            list = mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将对象转化成json串
     * @param obj   源对象
     * @return      json字符串
     */
    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}