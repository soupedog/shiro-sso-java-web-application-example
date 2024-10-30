package io.github.soupedog.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class JsonUtil {
    /**
     * 去除空白、换行方式输出
     */
    public static final ObjectMapper mapper = new ObjectMapper();
    /**
     * 自动换行方便人工阅读方式输出
     */
    public static final ObjectMapper mapperForIndent = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        defaultConfig(mapper);

        mapperForIndent.configure(SerializationFeature.INDENT_OUTPUT, true);
        defaultConfig(mapperForIndent);
    }

    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static void defaultConfig(ObjectMapper target) {
        // 遇到多余属性反序列化时也不认为错误
        target.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许数字 0 开头
        target.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
        target.registerModule(new JavaTimeModule());
        target.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        target.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略序列化时为 null 的属性
        target.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String formatAsString(Object target) {
        return formatAsString(false, target);
    }

    public static String formatAsString(boolean enableIndent, Object target) {
        ObjectMapper currentObjectMapper = enableIndent ? mapperForIndent : mapper;
        try {
            String result;
            if (target instanceof String) {
                if (!StringUtils.hasText((String) target)) {
                    return (String) target;
                }
                String firstVal = target.toString().substring(0, 1);
                switch (firstVal) {
                    case "[":
                        result = currentObjectMapper.writeValueAsString(currentObjectMapper.readValue((String) target, List.class));
                        break;
                    case "{":
                        result = currentObjectMapper.writeValueAsString(currentObjectMapper.readValue((String) target, HashMap.class));
                        break;
                    default:
                        throw new RuntimeException("JsonHelper fail to format: " + target);
                }
            } else {
                result = currentObjectMapper.writeValueAsString(target);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("JsonHelper fail to format: " + target, e);
        }
    }

    public static <T> T readAsObject(String jsonTarget, Class<T> tClass) {
        if (jsonTarget == null) {
            return null;
        }
        try {
            if (String.class.equals(tClass)) {
                return (T) jsonTarget;
            }
            return mapper.readValue(jsonTarget, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonHelper fail to readValue(" + tClass.getName() + ").", e);
        }
    }

    public static <T> T readAsObject(String jsonTarget, TypeReference<T> typeReference) {
        if (jsonTarget == null) {
            return null;
        }
        try {
            return mapper.readValue(jsonTarget, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonHelper fail to readValue(" + typeReference.getType().getTypeName() + ").", e);
        }
    }
}
