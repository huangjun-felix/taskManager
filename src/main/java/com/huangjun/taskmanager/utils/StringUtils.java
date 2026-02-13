package com.huangjun.taskmanager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class StringUtils {
    private StringUtils() {}

    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    private static final ThreadLocal<ObjectMapper> mapper = ThreadLocal.withInitial(()->{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    });

    public static String toJson(Object object) {
        try {
            return mapper.get().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return mapper.get().readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T>  fromListJson(List<String> jsonList, Class<T> clazz) {
        if (jsonList == null || jsonList.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> resultList = new ArrayList<>();
        try {
            ObjectMapper objectMapper = mapper.get();
            for (String json : jsonList) {
                if (json != null && !json.isEmpty()) {
                    T obj = objectMapper.readValue(json, clazz);
                    resultList.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JSON 反序列化失败", e);
        }
        return resultList;
    }
}
