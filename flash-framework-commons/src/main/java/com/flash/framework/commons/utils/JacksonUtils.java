package com.flash.framework.commons.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhurg
 * @date 2020/4/21 - 9:10 PM
 */
@Slf4j
public class JacksonUtils {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * 解析json
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T parseObject(String json, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * 转化成json
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String toJSONString(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}