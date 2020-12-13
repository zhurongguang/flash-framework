package com.flash.framework.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author zhurg
 * @date 2020/4/21 - 9:10 PM
 */
@Slf4j
public class JacksonUtils {

    public static final JacksonUtils JSON_NON_EMPTY_MAPPER;
    public static final JacksonUtils JSON_NON_DEFAULT_MAPPER;
    private ObjectMapper mapper;

    private JacksonUtils(JsonInclude.Include include) {
        JsonFactory jf = new JsonFactory();
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jf.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        this.mapper = new ObjectMapper(jf);
        this.mapper.setSerializationInclusion(include);
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static JacksonUtils nonEmptyMapper() {
        return JSON_NON_EMPTY_MAPPER;
    }

    public static JacksonUtils nonDefaultMapper() {
        return JSON_NON_DEFAULT_MAPPER;
    }

    public String toJson(Object object) {
        try {
            return object == null ? null : this.mapper.writeValueAsString(object);
        } catch (IOException var3) {
            log.warn("write to json string error:" + object, var3);
            return null;
        }
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, clazz);
            } catch (IOException var4) {
                log.warn("parse json string error:" + jsonString, var4);
                return null;
            }
        }
    }

    public <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, typeReference);
            } catch (Exception var4) {
                log.warn("parse json string error:" + jsonString, var4);
                return null;
            }
        }
    }

    public JsonNode treeFromJson(String jsonString) throws IOException {
        return this.mapper.readTree(jsonString);
    }

    public <T> T treeToValue(JsonNode node, Class<T> clazz) throws JsonProcessingException {
        return this.mapper.treeToValue(node, clazz);
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    static {
        JSON_NON_EMPTY_MAPPER = new JacksonUtils(JsonInclude.Include.NON_EMPTY);
        JSON_NON_DEFAULT_MAPPER = new JacksonUtils(JsonInclude.Include.NON_DEFAULT);
    }
}