package com.alan.kafkaexample.admin.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author Administrator
 */
public class JsonUtil {

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }
}
