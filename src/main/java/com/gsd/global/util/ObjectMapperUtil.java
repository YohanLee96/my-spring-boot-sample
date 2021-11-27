package com.gsd.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Yohan lee
 * Created on 2021/11/28.
 **/
public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static String toJSON(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static  <T> T toObject(String json, Class<T> clazz) {
        try {
            if(json != null) {
                return objectMapper.readValue(json, clazz);
            }

            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
