package com.yy.tools;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yy.common.ApiResponse;

/**
 * @ClassName: JsonPaserTool
 * @author: yangfeng
 * @date 2025/12/3 16:09
 * @version: 1.0.0
 */
public class JsonParserTool {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static  <T> ApiResponse<T> parseStrToObj(String json, Class<T> dataType) throws Exception {
        return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, dataType));
    }

    public static <T> T parseToEntity(String json, Class<T> dataType) throws Exception {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, dataType);
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败: " + e.getMessage(), e);
        }
    }
}
