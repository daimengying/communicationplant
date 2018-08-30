package com.ahyx.wechat.communicationplant.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: daimengying
 * @Date: 2018/8/8 16:04
 * @Description:对象转换
 */
public class ObjectUtils {

    /**
     * 对象转TreeMap
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToTreeMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new TreeMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
