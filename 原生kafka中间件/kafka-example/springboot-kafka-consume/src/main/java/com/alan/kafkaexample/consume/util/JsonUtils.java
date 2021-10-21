package com.alan.kafkaexample.consume.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月01日 16:13
 */
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T toObject(String json, Class<T> cls) {
        try {
            return OBJECT_MAPPER.readValue(json, cls);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json字符串的key转驼峰式命名
     * @param json
     */
    public static void convertCamelName(Object json) {
        if (json instanceof JSONArray) {
            JSONArray arr = (JSONArray) json;
            for (Object obj : arr) {
                convertCamelName(obj);
            }
        } else if (json instanceof JSONObject) {
            JSONObject jo = (JSONObject) json;
            Set<String> keys = jo.keySet();
            String[] array = keys.toArray(new String[keys.size()]);
            for (String key : array) {
                Object value = jo.get(key);
                String[] key_strs = key.split("_");
                if (key_strs.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < key_strs.length; i++) {
                        String ks = key_strs[i];
                        if (!"".equals(ks)) {
                            if (i == 0) {
                                sb.append(ks);
                            } else {
                                int c = ks.charAt(0);
                                if (c >= 97 && c <= 122) {
                                    int v = c - 32;
                                    sb.append((char) v);
                                    if (ks.length() > 1) {
                                        sb.append(ks.substring(1));
                                    }
                                } else {
                                    sb.append(ks);
                                }
                            }
                        }
                    }
                    jo.remove(key);
                    jo.put(sb.toString(), value);
                }
                convertCamelName(value);
            }
        }
    }
}
